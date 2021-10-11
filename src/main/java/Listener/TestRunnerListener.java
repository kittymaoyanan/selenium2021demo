package Listener;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.collections.Lists;
import utilities.TestNGRetry;

import java.util.Collections;

public class TestRunnerListener extends TestListenerAdapter {
    @Override
    public void onTestSuccess(ITestResult var1) {
        TestNGRetry retryAnalyzer = (TestNGRetry) var1.getMethod().getRetryAnalyzer();
        retryAnalyzer.reSetCount();
        Collections.synchronizedList(Lists.newArrayList()).add(var1.getMethod());
        Collections.synchronizedList(Lists.newArrayList());
    }


    @Override
    public void onTestFailure(ITestResult var1) {
        TestNGRetry retryAnalyzer = (TestNGRetry) var1.getMethod().getRetryAnalyzer();
        retryAnalyzer.reSetCount();
        Collections.synchronizedList(Lists.newArrayList()).add(var1.getMethod());
        Collections.synchronizedList(Lists.newArrayList());
    }
}