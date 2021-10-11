package utilities;

import org.apache.commons.lang.StringUtils;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.Reporter;

public class TestNGRetry implements IRetryAnalyzer {

    private int retryCount = 1;//设置当前的重跑次数
//    private static final int maxRetryCount = 0;//设置最大重跑次数，定义为常量。
    private int maxRetryCount;
    String failRetry = System.getProperty("failRetry");

    @Override
    public boolean retry(ITestResult iTestResult) {
        if(StringUtils.isEmpty(failRetry)) {
            this.maxRetryCount = 0;
        } else {
            this.maxRetryCount = Integer.parseInt(failRetry);
        }
        if (retryCount<=maxRetryCount){
            System.out.println("it's the "+retryCount+"times retry.And the current case is"+iTestResult.getName());//输出当前的重跑次数以及当前的正在重跑的用例名称。
            Reporter.setCurrentTestResult(iTestResult);
            Reporter.log("RunCount="+(retryCount+1));
            retryCount++;//重跑之后，次数+1
            return true;//当return true之后，代表继续重跑
        }
        return false;//return false之后，代表停止重跑
    }

    public void reSetCount(){
        retryCount=1;
    }

}