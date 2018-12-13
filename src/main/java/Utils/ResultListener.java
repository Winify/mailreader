package Utils;

import lombok.extern.slf4j.Slf4j;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

@Slf4j
public class ResultListener extends TestListenerAdapter {

    @Override
    public void onTestSuccess(ITestResult result) {
        super.onTestSuccess(result);

        log.info("Successful: " + result.getTestClass().getName() + " - " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        super.onTestFailure(result);

        String testName = result.getTestClass().getName() + "." + result.getMethod().getMethodName();

        log.error("Error while running [" + testName + "]");
    }
}
