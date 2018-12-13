package Email;

import javax.mail.MessagingException;
import javax.mail.Part;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.List;

public class Email {

    private String from;
    private String to;
    private String subject;
    private String recievedDate;

    private List<Part> parts;

    Email(String from, String to, String subject, String receivedDate, List<Part> parts) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.recievedDate = receivedDate;
        this.parts = parts;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getRecievedDate() {
        return recievedDate;
    }

    public String getPartContentTypeByIndex(int i) throws MessagingException {
        return parts.get(i).getContentType();
    }

    String getPartMessageByIndex(int i) throws Exception {
        return getPartMessage(parts.get(i));
    }

    private String getPartMessage(Part p) throws Exception {
        StringBuilder consumer = new StringBuilder();

        InputStream is = p.getInputStream();
        if (!(is instanceof BufferedInputStream)) is = new BufferedInputStream(is);

        int character;
        while ((character = is.read()) != -1) {
            consumer.append(character);
        }

        return consumer.toString();
    }
}
