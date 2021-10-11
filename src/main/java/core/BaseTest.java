package core;

import com.dt.web.DtTestCase;
import io.qameta.allure.Allure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.collections.Maps;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Properties;


public class BaseTest {
    public static ThreadLocal<WebDriver> driver = new ThreadLocal();
    public WebDriverWait wait;
    public DtTestCase dt;
    ChromeDriverService service;
    public Map<String, List<String>[]> steps = Maps.newHashMap();
    public int stepIndex = 0;
    public  StringBuffer sb = new StringBuffer();

    public void addStep(String methodName, List<String> ids, List<String> pages) {
        List<String>[] tmp = new List[2];
        tmp[0] = ids;
        tmp[1] = pages;
        steps.put(methodName, tmp);
    }

    public void refreshStep() {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        if (steps.containsKey(methodName)) {
            if (steps.containsKey(methodName) && steps.get(methodName).length > 0 ) {
                steps.get(Thread.currentThread().getStackTrace()[2].getMethodName())[0].remove(0);
                steps.get(Thread.currentThread().getStackTrace()[2].getMethodName())[1].remove(0);
            }
        }
        return;
    }

    public String getCurrentPage() {
        String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
        if (steps.containsKey(methodName)) {
            if (steps.containsKey(methodName) && steps.get(methodName).length > 0 ) {
                return steps.get(methodName)[1].get(0);
            }
        }
        return  null;
    }


    public WebDriver getDriver() {
        return driver.get();
    }

    @BeforeClass(description = "Class Level Setup!")
    public void setUp(String URL) throws Exception {
        String drivercho = getKey("driver", "chrome");
        try {
            String driverN = System.getProperty("driver");
            String systemName = System.getProperty("os.name").toLowerCase();
            if(true) {


                if (systemName.contains("window")) {
                    if (drivercho.equals("0")) {
                        System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe"); //Chrome Driver
                        System.setProperty("webdriver.chrome.silentOutput", "true"); //Chrome Driver

                        if (StringUtils.isEmpty(URL)) {
                            driver.set(new ChromeDriver());
                        } else {
                            driver.set(new RemoteWebDriver(new URL(URL), DesiredCapabilities.chrome()));
                        }

                    } else if (drivercho.equals("1")) {

                        System.setProperty("webdriver.ie.driver", "C:\\Program Files\\Internet Explorer\\IEDriverServer.exe");

                        System.setProperty("webdriver.ie.silentOutput", "true"); //IE Driver
                        System.setProperty("webdriver.ie.nativeEvent", "false"); //IE Driver
                        if (StringUtils.isEmpty(URL)) {
                            driver.set(new InternetExplorerDriver());
                        } else {
                            driver.set(new RemoteWebDriver(new URL(URL), DesiredCapabilities.internetExplorer()));
                        }
                    }
                } else if (systemName.contains("linux")) {
                    System.setProperty("webdriver.chrome.driver", "/root/chrome/chromedriver"); //Chrome Driver
//                System.setProperty("webdriver.chrome.driver", "/home/jenkins/chromdriver"); //Chrome Driver
                    System.setProperty("webdriver.gecko.driver", "/usr/bin/google-chrome");
                    System.setProperty("webdriver.chrome.silentOutput", "true"); //Chrome Driver
                    ChromeOptions options = new ChromeOptions();
//                options.setExperimentalOption("useAutomationExtension", false);
//                options.addArguments("--headless");
                    options.addArguments("--no-sandbox");
//                options.addArguments("--disable-dev-shm-usage");
                    if (StringUtils.isEmpty(URL)) {
                        driver.set(new ChromeDriver(options));
                    } else {
                        driver.set(new RemoteWebDriver(new URL(URL), DesiredCapabilities.chrome()));
                    }
                } else if (systemName.contains("mac os x")) {
//                ChromeDriverService service = new ChromeDriverService.Builder().usingDriverExecutable(new File("/usr/local/bin/chromedriver")).usingAnyFreePort().build();
                    System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver"); //Chrome Driver
                    System.setProperty("webdriver.chrome.silentOutput", "true"); //Chrome Driver
                    driver.set(new ChromeDriver());
                } else {
                    throw new RuntimeException("unsupport platform");
                }
                //等待时间
                wait = new WebDriverWait(getDriver(), 0);
                //隐性等待
//            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
                dt = new DtTestCase(getDriver(), this);
                //最大化窗口
                getDriver().manage().window().maximize();
            }else {

                if (systemName.contains("window")) {
                    if (driverN.equals("0")) {
                        System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe"); //Chrome Driver
                        System.setProperty("webdriver.chrome.silentOutput", "true"); //Chrome Driver

                        if (StringUtils.isEmpty(URL)) {
                            driver.set(new ChromeDriver());
                        } else {
                            driver.set(new RemoteWebDriver(new URL(URL), DesiredCapabilities.chrome()));
                        }

                    } else if (driverN.equals("1")) {

                        System.setProperty("webdriver.ie.driver", "C:\\Program Files\\Internet Explorer\\IEDriverServer.exe");

                        System.setProperty("webdriver.ie.silentOutput", "true"); //IE Driver
                        System.setProperty("webdriver.ie.nativeEvent", "false"); //IE Driver
                        if (StringUtils.isEmpty(URL)) {
                            driver.set(new InternetExplorerDriver());
                        } else {
                            driver.set(new RemoteWebDriver(new URL(URL), DesiredCapabilities.internetExplorer()));
                        }
                    }else if (driverN.equals("2")) {

                        System.setProperty("webdriver.gecko.driver", "C:\\Program Files\\Mozilla Firefox\\geckodriver.exe");


                        if(StringUtils.isEmpty(URL))
                        {
                            driver.set(new FirefoxDriver());
                        }
                        else
                        {
                            driver.set(new RemoteWebDriver(new URL(URL), DesiredCapabilities.firefox()));
                        }
                    }else if (driverN.equals("3")) {

                        System.setProperty("webdriver.edge.driver", "C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedgedriver.exe");


                        if(StringUtils.isEmpty(URL))
                        {
                            driver.set(new EdgeDriver());
                        }
                        else
                        {
                            driver.set(new RemoteWebDriver(new URL(URL), DesiredCapabilities.edge()));
                        }
                    }
                }
                wait = new WebDriverWait(getDriver(), 0);
                //隐性等待
//            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
                dt = new DtTestCase(getDriver(), this);
                //最大化窗口
                getDriver().manage().window().maximize();
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public static String getKey(String key) {
        ClassLoader classLoader = com.dt.web.DtTestCase.class.getClassLoader();
        String fileName = "config.properties";
        InputStream in = classLoader.getResourceAsStream(fileName);
        Properties prop = new Properties();
        try {
            prop.load(in); /// 加载属性列表
            String returnkKey = prop.getProperty(key);
            in.close();
            return returnkKey;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public static String getKey(String key, String defaultValue) {
        String value = getKey(key);
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        }
        return value;
    }

    @AfterClass(description = "Class Level Teardown!")
    public void tearDown() throws Exception {
        getDriver().quit();
    }
    public void beforeEnd()
    {
        if(sb.length() > 0) {
            Allure.addAttachment("statistics", "txt", sb.toString());
            int  sb_length = sb.length();
            sb.delete(0,sb_length);
        }
    }


}
