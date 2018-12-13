import Email.Email;
import Email.MailReader;
import Utils.TestBase;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.PropertyConfigurator;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import javax.mail.MessagingException;

@Slf4j
public class GmailTest extends TestBase {

    @BeforeMethod
    @Parameters({"email", "emailPassword"})
    public void setUp(String email, String password) throws MessagingException {
        PropertyConfigurator.configure("log4j.properties");

        log.info("Open Java Mail");
        mail = new MailReader(email, password);
    }

    @AfterMethod
    public void tearDown(ITestResult result) throws MessagingException {
        closeJavaMail();
    }

    @Test()
    @Parameters({"email"})
    public void shouldReadLastUnreadGmailThenDeleteIt(String email) throws Exception {

        log.info("Get last unread email");
        Email lastUnreadEmail = mail.getLastUnreadEmail();

        log.info("Asserting email");
        Assert.assertEquals(lastUnreadEmail.getFrom(), "[// sender email]");
        Assert.assertEquals(lastUnreadEmail.getTo(), "[" + email + "]");
        Assert.assertTrue(lastUnreadEmail.getSubject().contains("Test Subject"));
        Assert.assertEquals(lastUnreadEmail.getPartContentTypeByIndex(1), "APPLICATION/PDF");
        Assert.assertEquals(lastUnreadEmail.getPartContentTypeByIndex(2), "APPLICATION/PDF2");
    }
}
