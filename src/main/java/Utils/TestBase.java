package Utils;

import Email.MailReader;
import lombok.extern.slf4j.Slf4j;

import javax.mail.MessagingException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class TestBase {

    protected static final String CURRENT_TIME = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

    protected MailReader mail;


    protected void closeJavaMail() throws MessagingException {
        mail.close();
    }

}
