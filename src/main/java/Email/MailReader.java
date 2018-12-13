package Email;

import lombok.extern.slf4j.Slf4j;

import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Slf4j
public class MailReader {

    private Store store;
    private Folder inbox;
    private Message[] messages;

    public MailReader(String email, String password) throws MessagingException {
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");

        Session session = Session.getDefaultInstance(props, null);
        store = session.getStore("imaps");
        store.connect("imap.gmail.com", email, password);
    }

    public void close() throws MessagingException {
        for (Message message : messages) message.setFlag(Flags.Flag.DELETED, true);

        inbox.close(true);
        store.close();
    }

    public Email getLastUnreadEmail() throws Exception {

        int elapsedTime = 0;
        int unreadCount = fetchUnreadMessages();
        log.info("Unread email count: " + unreadCount);

        while (unreadCount < 1 && elapsedTime <= 600) {
            inbox.close(true);

            elapsedTime += 5;
            Thread.sleep(5000);

            unreadCount = fetchUnreadMessages();
            log.info("Waiting for (" + elapsedTime + ") seconds; Unread email count: " + unreadCount);
        }

        if (unreadCount == 0) throw new Exception("No new emails in the last 10 minutes");

        Message lastmessage = messages[0];

        String from = Arrays.toString(lastmessage.getFrom());
        String to = Arrays.toString(lastmessage.getRecipients(Message.RecipientType.TO));
        String subject = lastmessage.getSubject();
        String receivedDate = new SimpleDateFormat("yyyyMMddHHmm").format(lastmessage.getReceivedDate());

        List<Part> parts = new ArrayList<>();
        Multipart multipart = (Multipart) lastmessage.getContent();

        for (int i = 0; i < multipart.getCount(); i++) {
            parts.add(multipart.getBodyPart(i));
        }

        return new Email(from, to, subject, receivedDate, parts);
    }

    private int fetchUnreadMessages() throws MessagingException {
        inbox = store.getFolder("Inbox");

        inbox.open(Folder.READ_WRITE);

        messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

        FetchProfile profile = new FetchProfile();
        profile.add(FetchProfile.Item.ENVELOPE);
        profile.add(FetchProfile.Item.CONTENT_INFO);

        inbox.fetch(messages, profile);

        return inbox.getUnreadMessageCount();
    }
}