package com.dt.web;

/*此测试工具系龙测科技（北京）有限公司独立开发软件，龙测科技（北京）有限公司依法独立享有该软件之 所有权利，此软件为商业软件提供付费使用。该软件使用者（含个人、法人或其它组织）：
 *
 1. 非经龙测科技（北京）有限公司授权许可，不得将之用于盈利或非盈利性的任何用途。
 2. 使用该软件必须保留龙测科技的版权声明，将该软件从原有自然语言文 字转换成另一自然语言文字的，仍应注明出处，并不得向任何第三方提供修改后的软件。
 3. 不得有其他侵犯龙测科技软件版权之行为。

   凡有上述及其他侵权行为的个人、法人或其它组织，必须立即停止侵权并对其侵权造成的一切不良后果承担全部责任。对此前，尤其是此后侵犯龙测科技版权的行为，
   龙测科技将依据《著作权法》、《计算机软件保护条例》 等相关法律、法规追究其经济责任和法律责任。
*/

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dt.model.LocationElement;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import core.BaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import redis.clients.jedis.Jedis;

//package com.company;

import org.opencv.core.*;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.*;
import java.util.Arrays;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import static dp.BasicDataprovider.generatePhone;
import static dp.BasicDataprovider.randomNum;

//package com.zhiqu.image.recognition;
//import org.opencv.highgui.Highgui;

/**
 * 简述: 龙测科技提供的Helper类
 */
public class DtTestCase {
    public static Logger logger = Logger.getLogger(com.dt.web.DtTestCase.class);


    static {
        String systemName = System.getProperty("os.name").toLowerCase();
        if(!systemName.contains("window")){
            String pathToAdd = "/usr/local/lib";
            try {
                final Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
                usrPathsField.setAccessible(true);

                //get array of paths
                final String[] paths = (String[]) usrPathsField.get(null);

                //check if the path to add is already present
                for (String path : paths) {
                    if (path.equals(pathToAdd)) {
                        continue;
                    }
                }

                //add the new path
                final String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
                newPaths[newPaths.length - 1] = pathToAdd;
                usrPathsField.set(null, newPaths);
            }
            catch (Exception e)
            {

            }
        }
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }


    public static String getKey(String key) {
        ClassLoader classLoader = com.dt.web.DtTestCase.class.getClassLoader();
        String fileName = "config.properties";
        InputStream in = classLoader.getResourceAsStream(fileName);
        Properties prop = new Properties();
        try {
            // 读取属性文件.properties
            // InputStream in = new BufferedInputStream(new
            // FileInputStream("config.properties"));
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

    public String getProjectRootDir() {
        try {
            File file = new File("");
            String rootDir = file.getCanonicalPath();
            if (rootDir.endsWith("/target")) {

                return rootDir.replace("/target", "");
            } else if (rootDir.endsWith("\\target")) {
                return rootDir.replace("\\target", "");
            }
            return rootDir;
        } catch (IOException e) {
            throw new RuntimeException("获取根路径失败");
        }

    }

    public void clearText(String locateWay, String locateValue, String locationElement, int times) {
        long startTime = new Date().getTime();
        String sttime = Long.toString(startTime);
        try {
            WebElement target = findView(locateWay, locateValue, locationElement);

            Robot robot = new Robot();
            sleep(500);
            target.click();
            for (int i = 0; i < times; i++) {

                robot.keyPress(KeyEvent.VK_BACK_SPACE);

                robot.keyRelease(KeyEvent.VK_BACK_SPACE);
            }


            //点击回车
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " clearText " + ++base.stepIndex + ","+sttime+","+sptime+",passed");
        } catch (Exception e) {
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " clearText " + ++base.stepIndex + ","+sttime+","+sptime+",fail");
            Assert.fail("定位值" + locateValue + "未找到", e);
            logger.error("未通过" + locateWay + "方式定位到元素：" + locateValue);
        }
    }

    public static int getIntByKey(String key, int defaultValue) {
        ClassLoader classLoader = com.dt.web.DtTestCase.class.getClassLoader();
        String fileName = "config.properties";
        InputStream in = classLoader.getResourceAsStream(fileName);
        Properties prop = new Properties();
        try {
            // 读取属性文件.properties
            // InputStream in = new BufferedInputStream(new
            // FileInputStream("config.properties"));
            prop.load(in); /// 加载属性列表
            String returnkKey = prop.getProperty(key);
            in.close();
            return Integer.parseInt(returnkKey);
        } catch (Exception e) {
            System.out.println(e);
            return defaultValue;
        }
    }

    private final static String TAG = "DragonTesting";
    private WebDriver driver;
    private boolean acceptNextAlert = true;
    // 数据库连接对象
    private static Connection Conn;
    // 存放xpath数组
    private List<String> strList = new ArrayList<String>();
    // 数据库host
    private static String Host = getKey("mysqladdress", "5");
    // 数据库的用户名
    private static String UserName = getKey("mysqlusername", "5");
    // 数据库的密码
    private static String Password = getKey("mysqlpassword", "5");
    //数据库端口
    private static String Port = getKey("mysqlport", "5");
    // 数据库database
    private static String Database = getKey("mysqldatabase", "5");
    // 数据库连接地址
    private static String URL = "jdbc:mysql://" + Host + ":" + Port + "/" + Database + "?characterEncoding=UTF-8";

    public int waitTime = (int) Integer.parseInt(getKey("wTime", "5"));

    public float thresholdvalue = (float) getIntByKey("thresholdvalue", 80) / 100;

    // 连接对象
    private Connection conn;
    // 传递sql语句
    private Statement stt;
    // 结果集
    private ResultSet set;

    private boolean screenshotCondition = Boolean.parseBoolean(getKey("screenshotCondition", "0"));

//    private static String stepScreenShot = getKey("stepScreenShot", "1");
    private static String stepScreenShot = "1";

    private BaseTest base;

    /**
     * 简述: 构造函数
     *
     * @param driver 关联的ActivityInstrumentationTestCase2实例
     */
    public DtTestCase(WebDriver driver, BaseTest base) {
        this.driver = driver;
        this.base = base;
    }

    /**
     * 方法的简述: 定位View
     *
     * @param locationElement 定位元素封装类
     * @return null 或者 找到的view
     */
    public WebElement findView(LocationElement locationElement) {
        return this.findView(locationElement.getUi(), locationElement.getText(), locationElement.getUiclass(),
                locationElement.getClassIndex(), locationElement.getCss());
    }

    /**
     * 方法的简述: 定位View
     *
     * @param id        根据ID定位View
     * @param text      根据Text定位View
     * @param viewclass 根据Class:Index定位View
     * @param index     配合class
     * @param css       配合css定位View
     * @return null 或者 找到的view
     */
    private WebElement findView(String id, String text, String viewclass, String index, String css) {
        WebElement v = null;

        if (!id.isEmpty()) {
            try {
                v = (WebElement) driver.findElement(By.id(id));
                return v;
            } catch (Exception error) {
                System.out.println("找不到 id=  " + text);
            }
        }

        if (!text.isEmpty()) {
            try {
                v = (WebElement) driver.findElement(By.name(text));
                return v;
            } catch (Exception error) {
                System.out.println("找不到 text=  " + text);
            }
        }

        if (!viewclass.isEmpty() && !index.isEmpty()) {
            int num = -1;
            try {
                num = Integer.parseInt(index);
            } catch (Exception e) {
                System.out.println("找不到 index=  " + index);
                return v;
            }

            List<?> viewList = driver.findElements(By.className(viewclass));
            if (viewList == null || (viewList.size() <= num)) {
                System.out.println("index 非法  " + index);
                return v;
            }
            return (WebElement) viewList.get(num);
        }
        if (!css.isEmpty()) {
            try {
                v = (WebElement) driver.findElement(By.cssSelector(css));
                return v;
            } catch (Exception error) {
                System.out.println("找不到 css=  " + css);
            }
        }
        return v;
    }

    public WebElement findView(String id, String text, String viewclass, String index) {
        WebElement v = null;

        if (!id.isEmpty()) {
            try {
                v = (WebElement) driver.findElement(By.id(id));
                return v;
            } catch (Exception error) {
                System.out.println("找不到 id=  " + text);
            }
        }

        if (!text.isEmpty()) {
            try {
                v = (WebElement) driver.findElement(By.name(text));
                return v;
            } catch (Exception error) {
                System.out.println("找不到 text=  " + text);
            }
        }

        if (!viewclass.isEmpty() && !index.isEmpty()) {
            int num = -1;
            try {
                num = Integer.parseInt(index);
            } catch (Exception e) {
                System.out.println("找不到 index=  " + index);
                return v;
            }

            List<?> viewList = driver.findElements(By.className(viewclass));
            if (viewList == null || (viewList.size() <= num)) {
                System.out.println("index 非法  " + index);
                return v;
            }
            return (WebElement) viewList.get(num);
        }
        return v;
    }

    /**
     * 方法的简述: 打开网页
     *
     * @param website 网址
     */
    public void get(String website) {
        this.driver.get(website);
        return;
    }

    /**
     * 方法的简述: 使用xpath直接定位，其他输入报错
     *
     * @param webview 中文文字或
     * @return null:没找到； By: 找到
     */
    public WebElement findWebView(String webview) {
        if (!webview.isEmpty()) {
            return driver.findElement(By.xpath(webview));
        }
        return null;
    }

    /**
     * 方法的简述: 把数字变为元素数组
     *
     * @param n 数字
     * @return 元素数组
     */
    private ArrayList<Integer> getDigitList(long n) {
        ArrayList<Integer> list = new ArrayList<Integer>();

        // Get each digit
        while (n > 0) {
            list.add((int) (n % 10));
            n = n / 10;
        }

        return list;
    }

    /**
     * 获取手机屏幕的宽度
     *
     * @return
     */
    public int getScreenWidth() {
        return driver.manage().window().getSize().width;
    }

    /**
     * 获取手机屏幕的高度
     *
     * @return
     */
    public int getScreenHeight() {
        return driver.manage().window().getSize().height;
    }

    /**
     * 查找控件
     *
     * @param locateWay
     * @param locateValue
     * @return
     */
    public WebElement findView(String locateWay, String locateValue, String locationElement) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, waitTime);
        WebElement element = null;
        long currentTime = 0L;
        long starttime = 0l;
        starttime = new Date().getTime();
        do {

            try {
                if (locateWay.equalsIgnoreCase("ID")) {
                    element = driver.findElement(By.id(locateValue));
                } else if (locateWay.equalsIgnoreCase("CLASS_NAME")) {
                    String[] value1 = locateValue.split(":");
                    List<?> viewList = driver.findElements(By.className(value1[0]));
                    int num = Integer.valueOf(value1[1]);
                    WebElement element1 = (WebElement) viewList.get(num);
                    element = element1;

                } else if (locateWay.equalsIgnoreCase("CSS_SELECTOR")) {
                    element = driver.findElement(By.cssSelector(locateValue));

                } else if (locateWay.equalsIgnoreCase("LINK_TEXT")) {
                    element = driver.findElement(By.linkText(locateValue));

                } else if (locateWay.equalsIgnoreCase("NAME")) {
                    element = driver.findElement(By.name(locateValue));

                } else if (locateWay.equalsIgnoreCase("PARTIAL_LINK_TEXT")) {
                    element = driver.findElement(By.partialLinkText(locateValue));

                } else if (locateWay.equalsIgnoreCase("TAGNAME")) {
                    element = driver.findElement(By.tagName(locateValue));

                } else if (locateWay.equals("DYNAMIC_XPATH")) {
                    String locateValue_n = XP(locateValue);
                    element = driver.findElement(By.xpath(locateValue_n));
                } else if (locateWay.equals("XPATH")) {
                    element = driver.findElement(By.xpath(locateValue));
                } else if (locateWay.equals("INTEGRATE")) {
                    JSONArray jsonArray = JSON.parseArray(locateValue);
                    for (Object obj : jsonArray) {
                        try {
                            JSONObject jsonObject = (JSONObject) obj;
                            List<WebElement> elements = findViews(jsonObject.getString("type").toUpperCase(), jsonObject.getString("value"));
                            if (elements.size() == 1) {
                                element = elements.get(0);
                                break;
                            }
                        } catch (Exception e) {
                            continue;
                        }
                    }
                }
                else {
                    logger.error("定位方式：" + locateWay + "不被支持");
                    Assert.fail("定位方式：" + locateWay + "不被支持");
                    element = null;
                }
            } catch (NoSuchElementException | TimeoutException e) {
//                Assert.fail("定位方式：" + locateWay + "超时");
                return element;
            }
            currentTime = new Date().getTime();
            Thread.sleep(500);
        } while (currentTime < starttime);

        return element;
    }

    public WebElement findViewForTime(String locateWay, String locateValue) {
        WebDriverWait wait = new WebDriverWait(driver, waitTime);
        WebElement element = null;
        try {
            if (locateWay.equalsIgnoreCase("ID")) {
                element = driver.findElement(By.id(locateValue));
            } else if (locateWay.equalsIgnoreCase("CLASS_NAME")) {
                String[] value1 = locateValue.split(":");
                List<?> viewList = driver.findElements(By.className(value1[0]));
                int num = Integer.valueOf(value1[1]);
                WebElement element1 = (WebElement) viewList.get(num);
                element = element1;

            } else if (locateWay.equalsIgnoreCase("CSS_SELECTOR")) {
                element = driver.findElement(By.cssSelector(locateValue));

            } else if (locateWay.equalsIgnoreCase("LINK_TEXT")) {
                element = driver.findElement(By.linkText(locateValue));

            } else if (locateWay.equalsIgnoreCase("NAME")) {
                element = driver.findElement(By.name(locateValue));

            } else if (locateWay.equalsIgnoreCase("PARTIAL_LINK_TEXT")) {
                element = driver.findElement(By.partialLinkText(locateValue));

            } else if (locateWay.equalsIgnoreCase("TAGNAME")) {
                element = driver.findElement(By.tagName(locateValue));

            } else if (locateWay.equals("DYNAMIC_XPATH")) {
                String locateValue_n = XP(locateValue);
                element = driver.findElement(By.xpath(locateValue_n));
            } else if (locateWay.equals("XPATH")) {
                element = driver.findElement(By.xpath(locateValue));
            } else {
                logger.error("定位方式：" + locateWay + "不被支持");
                Assert.fail("定位方式：" + locateWay + "不被支持");
                element = null;
            }
        } catch (NoSuchElementException | TimeoutException e) {
            return element;
        }
        return element;
    }

    public WebElement findView(String locateWay, String locateValue) {
        WebDriverWait wait = new WebDriverWait(driver, waitTime);
        WebElement element = null;

        if (locateWay.equalsIgnoreCase("ID")) {
            element = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(locateValue)));
        } else if (locateWay.equalsIgnoreCase("CLASS_NAME")) {
            String[] value1 = locateValue.split(":");
            List<?> viewList = driver.findElements(By.className(value1[0]));
            int num = Integer.valueOf(value1[1]);
            WebElement element1 = (WebElement) viewList.get(num);
            element = element1;

        } else if (locateWay.equalsIgnoreCase("CSS_SELECTOR")) {
            element = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(locateValue)));

        } else if (locateWay.equalsIgnoreCase("LINK_TEXT")) {
            element = wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText(locateValue)));

        } else if (locateWay.equalsIgnoreCase("NAME")) {
            element = wait.until(ExpectedConditions.presenceOfElementLocated(By.name(locateValue)));

        } else if (locateWay.equalsIgnoreCase("PARTIAL_LINK_TEXT")) {
            element = wait.until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText(locateValue)));

        } else if (locateWay.equalsIgnoreCase("TAGNAME")) {
            // element = driver.findElement(By.tagName(locateValue));
            element = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName(locateValue)));

        } else if (locateWay.equals("DYNAMIC_XPATH")) {
            String locateValue_n = XP(locateValue);
            element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locateValue_n)));
        } else if (locateWay.equals("XPATH")) {
            // element = driver.findElement(By.xpath(locateValue));
            element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locateValue)));
        } else {
            logger.error("定位方式：" + locateWay + "不被支持");
            Assert.fail("定位方式：" + locateWay + "不被支持");
            element = null;
        }

        return element;
    }

    /**
     * 查找控件
     *
     * @param locateWay
     * @param locateValue
     * @return
     */
    public List<WebElement> findViews(String locateWay, String locateValue) {
        WebElement element = null;
        try {
            Thread.sleep(1000);
            if (locateWay.equalsIgnoreCase(
                    "ID")) {
                return driver.findElements(By.id(locateValue));
            } else if (locateWay.equalsIgnoreCase("CLASS_NAME")) {
                return driver.findElements(By.className(locateValue));
            } else if (locateWay.equalsIgnoreCase("CSS_SELECTOR")) {
                return driver.findElements(By.cssSelector(locateValue));
            } else if (locateWay.equalsIgnoreCase("LINK_TEXT")) {
                // element = driver.findElement(By.linkText(locateValue));
                return driver.findElements(By.linkText(locateValue));
            } else if (locateWay.equalsIgnoreCase("NAME")) {
                // element = driver.findElement(By.name(locateValue));
                return driver.findElements(By.name(locateValue));
            } else if (locateWay.equalsIgnoreCase("PARTIAL_LINK_TEXT")) {
                // element = driver.findElement(By.partialLinkText(locateValue));
                return driver.findElements(By.partialLinkText(locateValue));
            } else if (locateWay.equalsIgnoreCase("TAGNAME")) {
                // element = driver.findElement(By.tagName(locateValue));
                return driver.findElements(By.tagName(locateValue));
            } else if (locateWay.equals("XPATH")) {
                // element = driver.findElement(By.xpath(locateValue));
                return driver.findElements(By.xpath(locateValue));
            } else {
                logger.error("定位方式：" + locateWay + "不被支持");
//                Assert.fail("定位方式：" + locateWay + "不被支持");
                element = null;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            throw e;
        }
        System.out.println("element" + element);
        return (List) element;
    }

    /**
     * 数据类型操作
     *
     * @param param 数据类型,操作方法,参数
     * @param type  返回值的类型
     * @return
     */
    private Object addDataAction(String value, String param, int type) {
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(param);
        if (element.isJsonObject()) {
            JsonObject object = element.getAsJsonObject();  // 转化为对象
            if (object.get("t") == null) {
                return value;
            } else if (object.get("f") == null) {
                return value;
            }
            String t = object.get("t").getAsString();
            String funcname = object.get("f").getAsString();
            String params = "";
            String[] paramLists = new String[]{};
            if (object.get("p") == null) {
                paramLists = params.split("");
            } else {
                params = object.get("p").getAsString();
                paramLists = params.split(",");
            }
            switch (t) {
                case "0"://字符串
                    switch (funcname) {
                        case "s"://字符串切割
                            if (paramLists.length == 2) {//切割后以
                                String result = value.split(paramLists[0])[Integer.valueOf(paramLists[1])];
                                switch (type) {
                                    case 1:
                                        return Float.valueOf(result);
                                    default:
                                        return String.valueOf(result);
                                }
                            } else {
                                logger.info("传入参数过多");
                                return "Error传入参数过多";
                            }
                        case "c"://字符串切片
                            switch (paramLists.length) {
                                case 1:
                                    return value.substring(Integer.valueOf(paramLists[0]));
                                case 0:
                                    return value.substring(Integer.valueOf(0));
                                default:
                                    return value.substring(Integer.valueOf(paramLists[0]),
                                            Integer.valueOf(paramLists[1]));
                            }
                        default:
                            return "Error字符串未支持的方法";
                    }
                case "1"://Map字典
                    switch (funcname) {
                        case "k"://根据key获取value
                            Gson gson = new Gson();
                            Map<String, Object> map = new HashMap<String, Object>();
                            map = gson.fromJson(value, map.getClass());
                            Object result = map.get(paramLists[0]);
                            switch (type) {
                                case 1:
                                    return (Float) result;
                                default:
                                    return result;
                            }
                        default:
                            return "Error 字典方法暂未支持的方法";
                    }
                case "2"://数组
                    String[] temp = value.split("");
                    switch (funcname) {
                        case "e"://遍历
                            String result = "";
                            for (String p : temp) {
                                result += p + ",";
                            }
                            return result;
                        case "i"://索引取值
                            int index = Integer.parseInt(paramLists[0]);
                            if (index >= temp.length) {
                                return "Error 索引超出变量长度";
                            } else {
                                String r = temp[index];
                                switch (type) {
                                    case 1:
                                        return Float.valueOf(r);
                                    default:
                                        return r;
                                }
                            }
                        default:
                            return "Error列表暂未支持的方法";
                    }
                case "":
                    return value;
                default:
                    return "Erro暂未支持的数据类型";
            }
        } else {
            logger.info(String.format("%s格式不正确", param));
            return String.format("%s格式不正确", param);
        }
    }

    public Object getValue(String locateWay, String locateValue, String locationElement, String param, int type) throws
            InterruptedException {
        String value = findView(locateWay, locateValue, locationElement).getText();
        this.saveScreenshotPost(base.getCurrentPage() + " getValue " + ++base.stepIndex);
        if (param.equals("") || param == null) {
            return value;
        } else {
            Object o = addDataAction(value, param, type);
            logger.info("数据操作返回结果:" + o);
            return o;
        }
    }

    public void x(String locateWay, String locateValue, String locationElement, String dateControlId) throws
            InterruptedException {
        long startTime = new Date().getTime();
        String sttime = Long.toString(startTime);
        try {
            String js = "document.getElementById(\"" + dateControlId + "\").removeAttribute(\"readonly\")";
            ((JavascriptExecutor) driver).executeScript(js);    //将driver强制转换为JavascriptExecutor类型
            logger.info("remove readonly");
            String date = getCurrentDate();
            logger.info("get current time");
            findView(locateWay, locateValue, locationElement).clear();
            findView(locateWay, locateValue, locationElement).sendKeys(date);
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " inputTime " + ++base.stepIndex + ","+sttime+","+sptime+",passed");
        }catch (Exception e){
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " inputTime " + ++base.stepIndex + ","+sttime+","+sptime+",fail");
            logger.error("inputTime失败");
            Assert.fail("inputTime失败", e);

        }
    }

    public String XP(String locateWay) {
        if (locateWay.equals("")) {
            logger.error("输入参数：" + locateWay + "不被支持");
            Assert.fail("输入参数：" + locateWay + "不被支持");
        }
        int length = locateWay.length();
        char ss[] = locateWay.toCharArray();
        String a = "";
        for (int i = 0; i < length; i++) {
            a = a + "[contains(text(),\'" + ss[i] + "\')]";
        }
        a = "//*" + a;
        System.out.println("a:" + a);
        return a;
    }

    private String getCurrentDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间
    }

    public void uploadFile(String locateWay, String locateValue, String locationElement, String text) {
        long startTime = new Date().getTime();
        String sttime = Long.toString(startTime);
        try {
            if (locateWay.equalsIgnoreCase("XY")) {
                String[] array = locateValue.split(",");
                int v_x = Integer.parseInt(array[0]);
                int v_y = Integer.parseInt(array[1]);

                upLoad_File_By_XY(text, v_x, v_y);

            } else {
                upLoad_File_By_Element(locateWay, locateValue, locationElement, text);

            }

            logger.info("通过" + locateWay + "方式定位到元素：" + locateValue + "输入值：" + text);
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " uploadFile " + ++base.stepIndex + ","+sttime+","+sptime+",passed");
        } catch (Exception e) {
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " uploadFile " + ++base.stepIndex + ","+sttime+","+sptime+",fail");
            Assert.fail("定位值" + locateValue + "未找到", e);
            logger.error("未通过" + locateWay + "方式定位到元素：" + locateValue);
        }
    }

    public void logDelay(String locateWay, String locateValue, String locationElement,String name) throws InterruptedException {
        long currentTime = 0L;
        long starttime = 0l;
        starttime = new Date().getTime();
        long totaltime = 0;

        do {
            try {
                Object A = findView(locateWay, locateValue);
                currentTime = new Date().getTime();
                totaltime = currentTime - starttime;
                if (A != null) {
                    float to = (float) (Math.round(totaltime)) / 1000;
                    String total = String.format("%.3f", to);
                    this.saveScreenshotPost(base.getCurrentPage() + " logDelay " + ++base.stepIndex + "业务用时：" + total);

                    long totalMilliSeconds = System.currentTimeMillis();
                    String total_time = String.valueOf(totalMilliSeconds);
                    base.sb.append(name + " " + total + " s " + total_time +"\n");
                    break;
                }
            } catch (Exception e) {

            }
            currentTime = new Date().getTime();
            Thread.sleep(500);
        } while (currentTime < starttime + 30000);
        if (currentTime > starttime + 30000) {
            this.saveScreenshotPost("Error:" + base.getCurrentPage() + " logDelay " + ++base.stepIndex + "业务用时超过预定时间30s");
            base.sb.append(name+" 0 s 0 1"+"\n");
            Assert.fail("logDelay failed");
        }

    }

    public void upLoad_File_By_Element(String locateWay, String locateValue, String locationElement, String text) {
        try {
            WebElement target = findView(locateWay, locateValue, locationElement);


            StringSelection selection = new StringSelection(text);
            //把路径复制到剪切板
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
            Robot robot = new Robot();
            sleep(500);

            Actions action = new Actions(driver);
            action.moveToElement(target).click().perform();
            Thread.sleep(8000);

            //按下Ctrl+V

            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);

            //释放Ctrl+V

            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_V);

            Thread.sleep(3000);
            //点击回车

            robot.keyPress(KeyEvent.VK_ENTER);

            robot.keyRelease(KeyEvent.VK_ENTER);

            logger.info("通过" + locateWay + "方式定位到元素：" + locateValue + "输入值：" + text);
        } catch (Exception e) {
            Assert.fail("定位值" + locateValue + "未找到", e);
            logger.error("未通过" + locateWay + "方式定位到元素：" + locateValue);
        }
    }

    public void upLoad_File_By_XY(String text, int x, int y) {
        try {


            StringSelection selection = new StringSelection(text);
            //把路径复制到剪切板
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
            Robot robot = new Robot();
            sleep(500);
            Actions action = new Actions(driver);
            action.moveByOffset(x, y).click().perform();
            Thread.sleep(1000);

            //按下Ctrl+V

            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);

            //释放Ctrl+V

            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_V);

            Thread.sleep(1000);
            //点击回车

            robot.keyPress(KeyEvent.VK_ENTER);

            robot.keyRelease(KeyEvent.VK_ENTER);
//            target.sendKeys(text);
            logger.info("通过" + "XY" + "方式定位到元素：" + x + "," + y + "输入值：" + text);
        } catch (Exception e) {
            Assert.fail("定位值" + x + "," + y + "未找到", e);
            logger.error("未通过" + "XY" + "方式定位到元素：" + x + "," + y);
        }
    }

    public void input_text(String locateWay, String locateValue, String locationElement, String text) {
        try {
            if (locateWay.equalsIgnoreCase("XY")) {
                String[] array = locateValue.split(",");
                int v_x = Integer.parseInt(array[0]);
                int v_y = Integer.parseInt(array[1]);

                inputText_More_XY(text, v_x, v_y);

            } if(locateWay.contains("IMAGE")){
                int a[] = match_pt(locateWay,locateValue);
                inputText_More_XY(text,a[0],a[1]);
            } else {
                inputText_More_Element(locateWay, locateValue, locationElement, text);

            }

            logger.info("通过" + locateWay + "方式定位到元素：" + locateValue + "输入值：" + text);
        } catch (Exception e) {
            Assert.fail("定位值" + locateValue + "未找到", e);
            logger.error("未通过" + locateWay + "方式定位到元素：" + locateValue);
        }
    }

    public void inputText_More_Element(String locateWay, String locateValue, String locationElement, String text) {
        try {
            WebElement target = findView(locateWay, locateValue, locationElement);

            StringSelection selection = new StringSelection(text);
            //把路径复制到剪切板
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
            Robot robot = new Robot();
            sleep(500);
            Actions action = new Actions(driver);
            action.moveToElement(target).click().perform();
            Thread.sleep(1000);

            //按下Ctrl+V

            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);

            //释放Ctrl+V

            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_V);

            Thread.sleep(500);

            logger.info("通过" + locateWay + "方式定位到元素：" + locateValue + "输入值：" + text);
        } catch (Exception e) {
            Assert.fail("定位值" + locateValue + "未找到", e);
            logger.error("未通过" + locateWay + "方式定位到元素：" + locateValue);
        }
    }

    public void inputText_More_XY(String text, int x, int y) {
        try {


            StringSelection selection = new StringSelection(text);
            //把路径复制到剪切板
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
            Robot robot = new Robot();
            sleep(500);
            Actions action = new Actions(driver);
            action.moveByOffset(x, y).click().perform();
            Thread.sleep(1000);

            //按下Ctrl+V

            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);

            //释放Ctrl+V

            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_V);

            Thread.sleep(500);

            logger.info("通过" + "XY" + "方式定位到元素：" + x + "," + y + "输入值：" + text);
        } catch (Exception e) {
            Assert.fail("定位值" + x + "," + y + "未找到", e);
            logger.error("未通过" + "XY" + "方式定位到元素：" + x + "," + y);
        }
    }

    public void click_enhance(String locateWay, String locateValue, String locationElement) {
        try {
            if (locateWay.equalsIgnoreCase("XY")) {
                String[] array = locateValue.split(",");
                int v_x = Integer.parseInt(array[0]);
                int v_y = Integer.parseInt(array[1]);

                move_By_XY(v_x, v_y);

            } else {

                moveClickByElement(locateWay, locateValue, locationElement);

            }
            this.saveScreenshotPost(base.getCurrentPage() + " click_enhance " + ++base.stepIndex);
            logger.info("通过" + locateWay + "方式定位到元locateWay, locateValue, locationElement素：" + locateValue);
        } catch (Exception e) {
            Assert.fail("定位值" + locateValue + "未找到", e);
            logger.error("未通过" + locateWay + "方式定位到元素：" + locateValue);
        }
    }

    public void moveClickByElement(String locateWay, String locateValue, String locationElement) throws
            InterruptedException {
        WebElement target = findView(locateWay, locateValue, locationElement);
        Actions action = new Actions(driver);
        action.moveToElement(target).click().perform();

    }

    public void click(String locateWay, String locateValue, String locationElement) {
        long startTime = new Date().getTime();
        String sttime = Long.toString(startTime);
        try {
            if (locateWay.equalsIgnoreCase("IMAGE")) {
                clickByImage(locateValue);
            } else if (locateWay.equals("DOUBLE_IMAGE")) {
                clickBy2Image(locateValue);
            } else {

                findView(locateWay, locateValue, locationElement).click();
                logger.info("通过" + locateWay + "方式定位到元素：" + locateValue + "点击：");
            }
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " click " + ++base.stepIndex + ","+sttime+","+sptime+",passed");
        } catch (NoSuchElementException | InterruptedException e) {
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " click " + ++base.stepIndex + ","+sttime+","+sptime+",fail");
            Assert.fail("定位值" + locateValue + "未找到", e);
            logger.error("未通过" + locateWay + "方式定位到元素：" + locateValue);
        }
    }
    public void clickByKey(String locateWay, String locateValue, String locationElement){
        long startTime = new Date().getTime();
        String sttime = Long.toString(startTime);
        try {
            if (!locateWay.equalsIgnoreCase("IMAGE") && !locateWay.equals("DOUBLE_IMAGE")) {


                WebElement element = findView(locateWay, locateValue, locationElement);
                element.sendKeys(Keys.ENTER);
                logger.info("通过" + locateWay + "方式定位到元素：" + locateValue + "点击：");
            }
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " click " + ++base.stepIndex + ","+sttime+","+sptime+",passed");
        } catch (NoSuchElementException | InterruptedException e) {
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " click " + ++base.stepIndex + ","+sttime+","+sptime+",fail");
            Assert.fail("定位值" + locateValue + "未找到", e);
            logger.error("未通过" + locateWay + "方式定位到元素：" + locateValue);
        }
    }

    public void clickInOrder(String locateWay, String locateValue, String locationElement, int slp) {
        if(locateWay == "INTEGRATE") {
            JSONArray jsonArray = JSON.parseArray(locateValue);
            for (Object obj : jsonArray) {
                try {
                    JSONObject jsonObject = (JSONObject) obj;
                    click(jsonObject.getString("type").toUpperCase(), jsonObject.getString("value"), locationElement);
                    sleep(slp);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
        else {
            List<WebElement> elements = findViews(locateWay, locateValue);
            if(elements.size()==0){
                logger.error("未通过" + locateWay + "方式定位到元素：" + locateValue);
                Assert.fail("定位值" + locateValue + "未找到");
            }
            try {
                for (WebElement w : elements) {
                    long startTime = new Date().getTime();
                    String sttime = Long.toString(startTime);
                    long stopTime = new Date().getTime();
                    String sptime = Long.toString(stopTime);
                    w.click();
                    sleep(slp);
                    logger.error("通过" + locateWay + "方式定位到元素：" + locateValue + "点击");
                    this.saveScreenshotPost(base.getCurrentPage() + " refreshUrl " + ++base.stepIndex + "," + sttime + "," + sptime + ",passed");
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private List<String> getList(String text, String replace){

        JSONArray jsonArray = JSON.parseArray(text);
        if(jsonArray.equals(null)){
            Assert.fail("clickDropDownBox方法元素异常，元素信息： " + text);
        }
        for (Object obj : jsonArray) {
            try {
                JSONObject jsonObject = (JSONObject) obj;
                String xp = jsonObject.getString("value");
                String pattern = "//(.*?)\\[text\\(\\)=\\'(.*?)\\'\\]";

                // 创建 Pattern 对象
                Pattern r = Pattern.compile(pattern);

                // 现在创建 matcher 对象
                Matcher m = r.matcher(xp);
                if (m.find()) {
                    String str1 = m.group(0);
                    String strEnd = str1.replace(m.group(2),replace);
                    strList.add(strEnd);

//                    System.out.println("Found value: " + m.group(0) );
//                    System.out.println("Found value: " + m.group(1) );
//                    System.out.println("Found value: " + m.group(2) );

                } else {
                    System.out.println("NO MATCH");
                }

                String pattern1 = "//(.*?)\\[contains\\(text\\(\\),\\'(.*?)\\'\\)\\]";

                // 创建 Pattern 对象
                Pattern r1 = Pattern.compile(pattern1);

                // 现在创建 matcher 对象
                Matcher m1 = r1.matcher(xp);
                if (m1.find()) {
                    String str2 = m1.group(0);
                    String strEnd2 = str2.replace(m1.group(2),replace);
                    strList.add(strEnd2);

//                    System.out.println("Found value: " + m1.group(0) );
//                    System.out.println("Found value: " + m1.group(1) );
//                    System.out.println("Found value: " + m1.group(2) );

                } else {
                    System.out.println("NO MATCH");
                }





            } catch (Exception e) {

            }
        }
        System.out.println(strList.toString());
        return strList;

    }
    public void clickDropDownBox(String locateWay, String locateValue, String locationElement, String text) throws InterruptedException {
        long startTime = new Date().getTime();
        String sttime = Long.toString(startTime);
        if(locateWay.equals("INTEGRATE")){
            List<String> list = getList(locateValue,text);
            if(list.size()==0 && locateValue.contains("select")) {
                selectSelectByVisibleText(locateWay, locateValue,locationElement,text);
            } else {
                for (String obj : list) {
                    String path = obj;
                    try {
                        findView("XPATH", path, locationElement).click();
                        logger.info("通过" + locateWay + "方式定位到元素：" + locateValue + "点击成功");
                        long stopTime = new Date().getTime();
                        String sptime = Long.toString(stopTime);
                        this.saveScreenshotPost(base.getCurrentPage() + " clickDropDownBox " + ++base.stepIndex + "," + sttime + "," + sptime + ",passed");
                        return;
                    } catch (Exception e) {
                        logger.info("clickDropDownBox ERROR" + e + "  ---可能元素定位中缺少符合标准的元素---");
                    }
                }
                long stopTime = new Date().getTime();
                String sptime = Long.toString(stopTime);
                this.saveScreenshotPost(base.getCurrentPage() + " clickDropDownBox " + ++base.stepIndex + "," + sttime + "," + sptime + ",fail");
                logger.info("通过" + locateWay + "方式定位到元素：" + locateValue + "失败");
                Assert.fail("通过" + locateWay + "方式定位到元素：" + locateValue + "失败");
            }
        }else {
            try {
                findView(locateWay, locateValue, locationElement).click();
                long stopTime = new Date().getTime();
                String sptime = Long.toString(stopTime);
                this.saveScreenshotPost(base.getCurrentPage() + " clickDropDownBox " + ++base.stepIndex + ","+sttime+","+sptime+",passed");

            } catch (Exception e) {
                logger.info("通过" + locateWay + "方式定位到元素：" + locateValue + "失败");

                long stopTime = new Date().getTime();
                String sptime = Long.toString(stopTime);
                this.saveScreenshotPost(base.getCurrentPage() + " clickDropDownBox " + ++base.stepIndex + ","+sttime+","+sptime+",fail");
                Assert.fail("通过" + locateWay + "方式定位到元素：" + locateValue + "失败");
            }

        }

    }
    public void refreshUrl() {
        long startTime = new Date().getTime();
        String sttime = Long.toString(startTime);
        try {
            driver.navigate().refresh();
            logger.info("页面刷新成功");
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " refreshUrl " + ++base.stepIndex + ","+sttime+","+sptime+",passed");
        } catch (Exception e) {
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " refreshUrl " + ++base.stepIndex + ","+sttime+","+sptime+",fail");
            Assert.fail("页面刷新失败", e);
            logger.error("页面刷新失败");
        }

    }

    public String getRandomTextFromList(String p) {
        try {
            int n = 1;
            List list = Arrays.asList(p.split(","));
            Map map = new HashMap();
            List listNew = new ArrayList();
            String result = "";
            if (list.size() <= n) {
                result = StringUtils.join(list, ",");
                return result;
            } else {
                while (map.size() < n) {
                    int random = (int) (Math.random() * list.size());
                    if (!map.containsKey(random)) {
                        map.put(random, "");
                        listNew.add(list.get(random));
                    }
                }
                result = StringUtils.join(listNew, ",");
                return result;
            }
        } catch (Exception e) {
            Assert.fail("获取失败", e);
        }
        return null;
    }

    public String getTimeStamp() {
        try {

            String timestamp = String.valueOf(System.currentTimeMillis());
            logger.info("获取时间戳成功");
//            this.saveScreenshotPost(base.getCurrentPage() + " getTimeStamp " + ++base.stepIndex);
            return timestamp;

        } catch (Exception e) {
            Assert.fail("获取时间戳失败", e);
            logger.error("获取时间戳失败");
        }
        return null;
    }

    public String getUUID(int count) {
        if (count > 32 | count < 0) {
            Assert.fail("UUID长度不能大于32或者小于0");
            logger.error("获取时间戳失败");
        } else {
            String uuid = UUID.randomUUID().toString().substring(0, count);
//            this.saveScreenshotPost(base.getCurrentPage() + " createUUID " + ++base.stepIndex);
            return uuid;
        }
        return null;
    }

    public String getRandomMobile() {
        String value = generatePhone();
        return value;
    }

    public int getRandomNum(int value1, int value2) {
        try {

            int value = randomNum(value1, value2);

            logger.info("生成随机数成功");
//            this.saveScreenshotPost(base.getCurrentPage() + " createRandomNum " + ++base.stepIndex);
            return value;

        } catch (Exception e) {
            Assert.fail("生成随机数失败", e);
            logger.error("生成随机数失败");
            return -1;
        }
    }

    public String combineText(String expression, Object... params) {
        try {
            String result = String.format(expression, params);
            logger.info("组合文本成功");
//            this.saveScreenshotPost(base.getCurrentPage() + " combineText " + ++base.stepIndex);
            return String.valueOf(result);
        } catch (Exception e) {
            Assert.fail("组合文本失败", e);
            logger.error("组合文本失败");
            return null;
        }
    }

    public void inputText(String locateWay, String locateValue, String locationElement, String text) {
        long startTime = new Date().getTime();
        String sttime = Long.toString(startTime);
        try {
//            WebElement target = findView(locateWay, locateValue, locationElement);
            if(locateWay.contains("IMAGE")){
                int a[] = match_pt(locateWay,locateValue);
                inputText_More_XY(text,a[0],a[1]);
            } else {


                findView(locateWay, locateValue, locationElement).clear();
                findView(locateWay, locateValue, locationElement).sendKeys(text);
//            target.clear();
//            target.sendKeys(text);
                logger.info("通过" + locateWay + "方式定位到元素：" + locateValue + "输入值：" + text);
                long stopTime = new Date().getTime();
                String sptime = Long.toString(stopTime);
                this.saveScreenshotPost(base.getCurrentPage() + " inputText " + ++base.stepIndex + "," + sttime + "," + sptime + ",passed");
            }
        } catch (Exception e) {
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " inputText " + ++base.stepIndex + ","+sttime+","+sptime+",fail");
            Assert.fail("定位值" + locateValue + "未找到", e);
            logger.error("未通过" + locateWay + "方式定位到元素：" + locateValue);
        }
    }

    public void inputByAutoIT(String locateWay, String locateValue, String locationElement, String exepath) {
        long startTime = new Date().getTime();
        String sttime = Long.toString(startTime);
        try {
            click(locateWay, locateValue, locationElement);
            sleep(1000);
            Runtime.getRuntime().exec(exepath);
            logger.info("执行上传文件通过");
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " inputByAutoIT " + ++base.stepIndex + ","+sttime+","+sptime+",passed");
        } catch (Exception e) {
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " inputByAutoIT " + ++base.stepIndex + ","+sttime+","+sptime+",fail");
            logger.error("执行上传文件失败");
        }
    }

    public void clickBy2Image(String path2) {
        try {
            int[] a = match_pt("DOUBLE_IMAGE", path2);
            move_By_XY(a[0], a[1]);
            logger.info("通过" + "IMAGE" + "定位到" + path2 + "并点击");
        } catch (AWTException e) {
            e.printStackTrace();
            logger.error("未通过" + "IMAGE" + "定位到" + path2);
        }
    }

    private void screenshortWhenAssert() {
        if (screenshotCondition) {
            saveScreenshotPNG();
        }
    }


    //断言相等
    public void assertDtEquals(String locateWay, String locateValue, String locationElement, Object text) throws Exception {
        if (locateWay.contains("XY") || locateWay.contains("IMAGE")) {
            logger.error("断言失败，不支持" + locateWay + "方式");
            Assert.fail("断言失败，不支持" + locateWay + "方式");
        } else {
            String expect = findView(locateWay, locateValue).getText();
            String actual = text.toString();
            try {
                screenshortWhenAssert();
                Assert.assertEquals(actual, expect);
            } catch (AssertionError e) {
                logger.error("实际值：" + actual + " ; 期望值：" + expect + "不相等");
                Assert.fail("实际值：" + actual + " ; 期望值：" + expect + "不相等");
                throw e;
            }
        }
    }

    //识别验证码
    public void getVerifyCodeAndLogin(String locateWay, String locateValue,String locationElement, String btnway, String btnvalue,String btnlocationElement, String url) {
        try {

        } catch (Exception e) {
            logger.error("获取验证码失败");
        }
    }

    public void getcodeAndLogin() {
        try {

        } catch (Exception e) {
            logger.error("获取验证码失败");
        }
    }

    public static boolean isNumber(String str) {

        // 该正则表达式可以匹配所有的数字 包括负数
        Pattern pattern = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");
        String bigStr;
        try {
            bigStr = new BigDecimal(str).toString();
        } catch (Exception e) {
            return false;//异常 说明包含非数字。
        }

        Matcher isNum = pattern.matcher(bigStr); // matcher是全匹配
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public boolean compare2values(String var,String value,int flag) {
        if(isNumber(var) && isNumber(value)) {

        }
        switch (flag){
            case 0:
                try {
                    Assert.assertEquals(value, var);
                    return true;
                } catch (AssertionError e) {
                    logger.error("实际值与期望值不相等");
                    return false;
                }
            case 1:
                try {
                    Assert.assertNotEquals(value, var);
                    return true;
                } catch (AssertionError e) {
                    logger.error("期望值等于实际值");
                    return false;
                }
            case 2:
                try {
                    assert (value.contains(String.valueOf(var)));
                    return true;
                } catch (AssertionError e) {
                    logger.error("期望值未包含实际值");
                    return false;
                }
        }
        return true;

    }


    /**
     * 断言值不相等
     *
     * @param locateWay
     * @param locateValue
     * @param text
     * @throws Exception
     */
    public void assertDtNotEquals(String locateWay, String locateValue, String locationElement, Object text) throws Exception {
        if (locateWay.contains("XY") || locateWay.contains("IMAGE")) {
            logger.error("断言失败，不支持" + locateWay + "方式");
            Assert.fail("断言失败，不支持" + locateWay + "方式");
        } else {
            String expect = findView(locateWay, locateValue).getText();
            String actual = text.toString();
            try {
                screenshortWhenAssert();
                Assert.assertNotEquals(actual, expect);
            } catch (AssertionError e) {
                logger.error("实际值：" + actual + " ; 期望值：" + expect + "相等");
                Assert.fail("实际值：" + actual + " ; 期望值：" + expect + "相等");
                throw e;
            }
        }


    }

    /**
     * 断言包含
     *
     * @param locateWay
     * @param locateValue
     * @param text
     * @throws Exception
     */
    public void assertDtContain(String locateWay, String locateValue, String locationElement, Object text) throws Exception {
        if (locateWay.contains("XY") || locateWay.contains("IMAGE")) {
            logger.error("断言失败，不支持" + locateWay + "方式");
            Assert.fail("断言失败，不支持" + locateWay + "方式");
        } else {
            String expect = findView(locateWay, locateValue).getText();
            String actual = text.toString();

            try {
                screenshortWhenAssert();
                assert (expect.contains(actual));
            } catch (AssertionError e) {
                logger.error("期望值未包含实际值");
                Assert.fail("期望值未包含实际值");
                throw e;
            }
        }

    }

    public int[] picture_2xy(String path1, String path2) throws IOException, AWTException, InterruptedException {

        String inputImg = getProjectRootDir() + "/images/element/" + path1;
        String[] baseImg_R = screen_for_IMAGE_R();
        String baseImg = baseImg_R[0];
        Mat baseTemplate = Imgcodecs.imread(baseImg);
        Mat inputMat = Imgcodecs.imread(inputImg);
        int w = inputMat.cols();
        int h = inputMat.rows();
        Mat result = Mat.zeros(baseTemplate.rows() - inputMat.rows() + 1, baseTemplate.cols() - inputMat.cols() + 1, CvType.CV_32FC1);
        Imgproc.matchTemplate(baseTemplate, inputMat, result, Imgproc.TM_CCOEFF);
        boolean exist = false;
        for (int i = 0; i < result.rows(); i++) {
            for (int j = 0; j < result.cols(); j++) {
                if (result.get(i, j)[0] >= 0.9) {

                    exist = true;
                    break;
                }
            }
            if (exist) {
                break;
            }
        }
        int x1 = 0;
        int y1 = 0;
        int x2 = 0;
        int y2 = 0;
        int x3 = 0;
        int y3 = 0;
        int x4 = 0;
        int y4 = 0;
        if (exist) {
            int o = 0;
            while (true) {
                int[] aa_2 = templete_R(0, inputImg, baseImg);
                x1 = aa_2[0];
                y1 = aa_2[1];
                x2 = aa_2[2];
                y2 = aa_2[3];
                cut(baseImg, x1, y1, x2, y2, baseImg_R[1]);
                boolean mm = intelligentlyWait_R(baseImg_R[1], inputImg);
                if (mm) {
                    break;
                } else {
                    if (o == 2) {
                        System.out.println(path1 + "元素定位失败");
                        logger.info(path1 + "元素定位不匹配");
                        throw new TimeoutException("元素定位失败");
                    } else {
                        sleep(500);
                    }

                }
                o += 1;
            }
            String inputImg_2 = getProjectRootDir() + "/images/element/" + path2;
            String baseImg_2 = baseImg_R[1];

            Mat baseTemplate_2 = Imgcodecs.imread(baseImg_2);
            Mat inputMat_2 = Imgcodecs.imread(inputImg_2);
            int w_2 = inputMat.cols();
            int h_2 = inputMat.rows();
            Mat result_2 = Mat.zeros(baseTemplate_2.rows() - inputMat_2.rows() + 1, baseTemplate_2.cols() - inputMat_2.cols() + 1, CvType.CV_32FC1);
            Imgproc.matchTemplate(baseTemplate_2, inputMat_2, result_2, Imgproc.TM_CCOEFF);
            boolean exist_2 = false;
            for (int i_2 = 0; i_2 < result_2.rows(); i_2++) {
                for (int j_2 = 0; j_2 < result_2.cols(); j_2++) {
                    if (result_2.get(i_2, j_2)[0] >= 0.98) {
                        System.out.println(result_2.get(i_2, j_2)[0]);
                        exist_2 = true;
                        break;
                    }
                }
                if (exist_2) {
                    break;
                }
            }
            int v_x_2 = 0;
            int v_y_2 = 0;

            if (exist_2) {
                while (true) {
                    int[] aa_3 = templete_R(0, inputImg_2, baseImg_2);
                    x3 = aa_3[0];
                    y3 = aa_3[1];
                    x4 = aa_3[2];
                    y4 = aa_3[3];
                    cut(baseImg_2, x3, y3, x4, y4, baseImg_R[2]);
                    boolean mm = intelligentlyWait_R(baseImg_R[2], inputImg_2);
                    if (mm) {
                        break;
                    } else {
                        if (o == 2) {
                            System.out.println(path2 + "元素定位失败");
                            logger.info(path2 + "元素定位不匹配");
                            throw new TimeoutException("元素定位失败");
                        } else {
                            sleep(500);
                        }

                    }
                    o += 1;
                }
            }

        }
        int X = Math.round(x1 + x3 + x4 / 2);
        int Y = Math.round(y1 + y3 + y4 / 2);
        return new int[]{X, Y};
    }

    /**
     * 断言alert的值是否与期望相等
     *
     * @param expect
     * @throws Exception
     */
    public void assertAlertMessage(String expect) throws Exception {
        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        alert.accept();
        try {
            screenshortWhenAssert();
            Assert.assertEquals(alertText,expect);
        } catch (AssertionError e) {
            logger.error("实际值：" + alertText + " ; 期望值：" + expect);
            Assert.fail("实际值：" + alertText + " ; 期望值：" + expect);
            throw e;
        }
    }

    /**
     * 断言元素是否被选中
     *
     * @param way
     * @param value
     * @throws Exception
     */
    public void assertIsSelected(String way, String value, String locationElement) throws Exception {
        try {
            screenshortWhenAssert();
            assert findView(way, value, locationElement).isSelected();
        } catch (AssertionError e) {
            logger.error("断言元素未被选中");
            Assert.fail("断言元素未被选中");
            throw e;
        }
    }

    /**
     * 断言元素未被选中
     *
     * @param way
     * @param value
     * @throws Exception
     */
    public void assertIsNotSelected(String way, String value, String locationElement) throws Exception {
        try {
            screenshortWhenAssert();
            assert findView(way, value, locationElement).isSelected() == false;
        } catch (AssertionError e) {
            logger.error("断言元素被选中了");
            Assert.fail("断言元素被选中了");
            throw e;
        }
    }

    public boolean is_image_display(String path) {
        try {
            int[] a = picture_xy(path);
            System.out.println(a);
            logger.info("通过" + "IMAGE" + "定位到" + path + "并点击");
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public boolean is_2image_display(String path1, String path2) {
        try {
            int[] a = picture_2xy(path1, path2);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void assertIsDisplayed(String way, String value, String locationElement) throws Exception {
        try {
            screenshortWhenAssert();
            if(way.contains("IMAGE")) {
                int a[] = match_pt(way,value);
                Assert.assertNotEquals(a[0],-1);
            }
            else {
                boolean res = findView(way, value, locationElement).isDisplayed();
                Assert.assertEquals(res,true);
            }
        } catch (AssertionError e) {
            logger.error("断言元素未显示");
            Assert.fail("断言元素未显示");
            throw e;
        } catch (Exception e) {
            logger.error("断言元素未显示");
            Assert.fail("断言元素未显示");
            throw e;
        }
    }

    public WebElement findView2(String locateWay, String locateValue)  {
        WebDriverWait wait = new WebDriverWait(driver, waitTime);
        WebElement element = null;
        long currentTime = 0L;
        long starttime = 0l;
        starttime = new Date().getTime();
        do {

            try {
                if (locateWay.equalsIgnoreCase("ID")) {
                    element = driver.findElement(By.id(locateValue));
                } else if (locateWay.equalsIgnoreCase("CLASS_NAME")) {
                    String[] value1 = locateValue.split(":");
                    List<?> viewList = driver.findElements(By.className(value1[0]));
                    int num = Integer.valueOf(value1[1]);
                    WebElement element1 = (WebElement) viewList.get(num);
                    element = element1;

                } else if (locateWay.equalsIgnoreCase("CSS_SELECTOR")) {
                    element = driver.findElement(By.cssSelector(locateValue));

                } else if (locateWay.equalsIgnoreCase("LINK_TEXT")) {
                    element = driver.findElement(By.linkText(locateValue));

                } else if (locateWay.equalsIgnoreCase("NAME")) {
                    element = driver.findElement(By.name(locateValue));

                } else if (locateWay.equalsIgnoreCase("PARTIAL_LINK_TEXT")) {
                    element = driver.findElement(By.partialLinkText(locateValue));

                } else if (locateWay.equalsIgnoreCase("TAGNAME")) {
                    element = driver.findElement(By.tagName(locateValue));

                } else if (locateWay.equals("DYNAMIC_XPATH")) {
                    String locateValue_n = XP(locateValue);
                    element = driver.findElement(By.xpath(locateValue_n));
                } else if (locateWay.equals("XPATH")) {
                    element = driver.findElement(By.xpath(locateValue));
                } else {
                    logger.error("定位方式：" + locateWay + "不被支持");
                    Assert.fail("定位方式：" + locateWay + "不被支持");
                    element = null;
                }
            } catch (NoSuchElementException | TimeoutException e) {

            }
            currentTime = new Date().getTime();
        } while (currentTime < starttime);

        return element;
    }

    public void assertIsNotDisplayed(String way, String value, String locationElement) throws Exception {
        try {
            screenshortWhenAssert();
            if(way.contains("IMAGE")) {
                int a[] = match_pt(way,value);
                Assert.assertEquals(a[0],-1);
            }
            else {
                try{
                    boolean res = findView2(way,value).isDisplayed();
                    Assert.assertEquals(res,false);
                }
                catch (Exception e) {

                }
//                boolean res = isElementExistOld(way, value, locationElement);
//                Assert.assertEquals(res,false);
            }

        } catch (Exception e) {
            logger.error("断言元素显示了");
            Assert.fail("断言元素显示了");
            throw e;
        }
    }

    public boolean is_text_present(String text, String time) throws InterruptedException {
        long times = Long.parseLong(time);
        long currentTime = 0L;
        long starttime = 0l;
        starttime = new Date().getTime();

        do {
            String locator = "//*[contains(text(), \"" + text + "\")]";
            try {
                driver.findElement(By.xpath(locator));
                return true;

            } catch (Exception e) {

            }
            currentTime = new Date().getTime();
            Thread.sleep(500);
        } while (currentTime < starttime + times);
        return false;
    }

    public boolean is_ele_present(String locateWay, String locateValue, String time) throws InterruptedException {
        long times = Long.parseLong(time);
        long currentTime = 0L;
        long starttime = 0l;
        starttime = new Date().getTime();
        do {

            try {
                findViewForTime(locateWay, locateValue);
                return true;

            } catch (Exception e) {

            }
            currentTime = new Date().getTime();
            Thread.sleep(500);
        } while (currentTime < starttime + times);
        return false;
    }

    /**
     * 判断元素是否存在
     */
    public boolean isElementExistOld(String locateWay, String locateValue, String locationElement) {
        try {
            findView(locateWay, locateValue, locationElement);
            logger.info("元素存在");
            return true;
        } catch (NoSuchElementException | InterruptedException ex) {
            logger.info("元素不存在");
            return false;
        }
    }

    public boolean isElementExist(String text, String time, String locationElement) {
        try {
            boolean end = is_text_present(text, time);

            return end;


        } catch (NoSuchElementException | InterruptedException ex) {

            return false;
        }
    }

    public boolean assertElementExist(String locateWay, String locateValue, String locationElement, int times) {
        try {
            String time = String.valueOf(times);
            boolean end = is_ele_present(locateWay, locateValue, time);
            if (end) {

            } else {
                Assert.fail("断言元素不存在");
            }
            return end;
        } catch (Exception ex) {
            Assert.fail("断言元素不存在");
            return false;
        }
    }

    public boolean assertElementExist(String text, int times, String locationElement) {
        try {
            String time = String.valueOf(times);
            boolean end = is_text_present(text, time);
            if (end) {
                return true;
            } else {

                Assert.fail("断言元素不存在");
                return true;
            }
        } catch (Exception ex) {
            Assert.fail("断言元素不存在");
            return false;
        }
    }

    public boolean waitElementExist(String locateWay, String locateValue, String locationElement, int times) {
        try {
            String time = String.valueOf(times);
            boolean end = is_ele_present(locateWay, locateValue, time);
            if (end) {

            } else {
                Assert.fail("断言元素不存在");
            }
            return end;
        } catch (Exception ex) {
            Assert.fail("断言元素不存在");
            return false;
        }
    }

    public boolean waitElementExist(String text, int times) {
        try {
            String time = String.valueOf(times);
            boolean end = is_text_present(text, time);
            if (end) {
                return true;
            } else {

                Assert.fail("断言元素不存在");
                return true;
            }
        } catch (Exception ex) {
            Assert.fail("断言元素不存在");
            return false;
        }
    }

    public boolean isElementExist(String locateWay, String locateValue, String locationElement, int times) {
        try {
            String time = String.valueOf(times);
            boolean end = is_ele_present(locateWay, locateValue, time);

            return end;
        } catch (NoSuchElementException | InterruptedException ex) {

            return false;
        }
    }

    public boolean isElementNotExist(String locateWay, String locateValue, String locationElement) {
        try {
            boolean end = is_ele_present(locateWay, locateValue, "30");
            if (end) {
                return false;
            } else {
                return true;
            }
        } catch (Exception ex) {
            logger.info("断言元素不存在");
            return true;
        }
    }

    public void assertDtRegionExist(String locateWay, String locateValue, String locationElement) throws Exception {
        if (locateWay.equals("IMAGE")) {
            try {
                boolean m = Region_picture_assertion_alone(locateValue);
                assert m == true;
            } catch (AssertionError m) {
                logger.error("断言元素不存在");
                throw m;
            }
        } else if (locateWay.equals("DOUBLE_IMAGE")) {
            try {
                String[] array = locateValue.split(",");
                String b = array[0];
                String a = array[1];
                boolean m = Region_picture_assertion_double(a, b);
                assert m == true;
            } catch (AssertionError m) {
                logger.error("断言元素不存在");
                throw m;
            }
        }

    }

    public void assertDtRegionNotExist(String locateWay, String locateValue, String locationElement) throws
            Exception {
        if (locateWay.equals("IMAGE")) {
            try {
                boolean m = Region_picture_assertion_alone(locateValue);
                assert m == false;
            } catch (AssertionError m) {
                logger.error("断言元素存在");
                throw m;
            }
        } else if (locateWay.equals("DOUBLE_IMAGE")) {
            try {
                String[] array = locateValue.split(",");
                String b = array[0];
                String a = array[1];
                boolean m = Region_picture_assertion_double(a, b);
                assert m == false;
            } catch (AssertionError m) {
                logger.error("断言元素存在");
                throw m;
            }
        }

    }

    public void assertIsEnabled(String way, String value, String locationElement) throws Exception {
        try {
            screenshortWhenAssert();
            assert findView(way, value, locationElement).isEnabled();
        } catch (AssertionError e) {
            logger.error("断言元素不可编辑");
            Assert.fail("断言元素不可编辑");
            throw e;
        }
    }

    public void assertIsNotEnabled(String way, String value, String locationElement) throws Exception {
        try {
            screenshortWhenAssert();
            assert findView(way, value, locationElement).isEnabled() == false;
        } catch (AssertionError e) {
            logger.error("断言元素可编辑");
            Assert.fail("断言元素可编辑");
            throw e;
        }
    }

    public void assertIsTitle(String title) throws Exception {
        String actual = driver.getTitle();
        try {
            screenshortWhenAssert();
            assert actual.equals(title);
        } catch (AssertionError e) {
            logger.error("实际值：" + actual + " ; 期望值：" + title);
            Assert.fail("实际值：" + actual + " ; 期望值：" + title);
            throw e;
        }
    }

    public void assertIsTitleContains(String title) throws Exception {
        String actual = driver.getTitle();
        try {
            screenshortWhenAssert();
            assert actual.contains(title);
        } catch (AssertionError e) {
            logger.error("实际值：" + actual + " ; 期望值：" + title);
            Assert.fail("实际值：" + actual + " ; 期望值：" + title);
            throw e;
        }
    }

    public void assertIsAlertPresent() throws Exception {
        try {
            screenshortWhenAssert();
            Alert alert = driver.switchTo().alert();
            alert.accept();
        } catch (NoAlertPresentException e) {
            logger.error("没有出现alert");
            Assert.fail("没有出现alert");
            throw e;
        }
    }

    public boolean isAlertPresent() {
        try {
            Alert alert = driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    public void assertAttribute(String way, String value, String locationElement, String attribute, String except) throws
            Exception {
        String actual = findView(way, value, locationElement).getAttribute(attribute);
        try {
            screenshortWhenAssert();
            assert actual.equals(except);
        } catch (AssertionError e) {
            logger.error("断言元素属性值不正确");
            Assert.fail("断言元素属性值不正确");
            throw e;
        }
    }

    public void dbClick(String locateWay, String locateValue, String locationElement) throws Exception {
        long startTime = new Date().getTime();
        String sttime = Long.toString(startTime);
        try {
            Actions actiondb = new Actions(driver);
            WebElement test1item = findView(locateWay, locateValue, locationElement);
            actiondb.doubleClick(test1item).perform();
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " dbClick " + ++base.stepIndex + ","+sttime+","+sptime+",passed");
        }catch (Exception e){
        long stopTime = new Date().getTime();
        String sptime = Long.toString(stopTime);
        this.saveScreenshotPost(base.getCurrentPage() + " dbClick " + ++base.stepIndex + ","+sttime+","+sptime+",fail");
            logger.error("dbClick"+"失败");
            Assert.fail("dbClick"+"失败");
        }
    }

    /**
     * 键盘回车
     */
    public void carriageReturn() {
        long startTime = new Date().getTime();
        String sttime = Long.toString(startTime);
        try {
            Actions action = new Actions(driver);
            action.sendKeys(Keys.ENTER).perform();
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " carriageReturn " + ++base.stepIndex + ","+sttime+","+sptime+",passed");
        } catch (Exception e){
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " carriageReturn " + ++base.stepIndex + ","+sttime+","+sptime+",fail");
            logger.error("carriageReturn"+"失败");
            Assert.fail("carriageReturn"+"失败");
        }
    }

    /**
     * 键盘事件
     */
    public void executeKeyBoard(String keys) {
        long startTime = new Date().getTime();
        String sttime = Long.toString(startTime);
        try {
            Actions action = new Actions(driver);
            action.sendKeys(keys).perform();
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " executeKeyBoard " + ++base.stepIndex + ","+sttime+","+sptime+",passed");
        }catch (Exception e){
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " executeKeyBoard " + ++base.stepIndex + ","+sttime+","+sptime+",fail");
            logger.error("executeKeyBoard"+"失败");
            Assert.fail("executeKeyBoard"+"失败");

        }
    }

    /**
     * 键盘事件
     */
    public void executeKeyBoard(String keys, String actions) {
        Actions action = new Actions(driver);
        action.sendKeys(keys, actions).perform();
    }

    /**
     * 鼠标右击
     *
     * @param locateWay   定位方式
     * @param locateValue 定位值
     */
    public void rightClick(String locateWay, String locateValue, String locationElement) throws Exception {
        long startTime = new Date().getTime();
        String sttime = Long.toString(startTime);
        try {
            Actions actiondb = new Actions(driver);
            WebElement test1item = findView(locateWay, locateValue, locationElement);
            actiondb.contextClick(test1item).perform();
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " rightClick " + ++base.stepIndex + ","+sttime+","+sptime+",passed");
        }catch (Exception e){
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " rightClick " + ++base.stepIndex + ","+sttime+","+sptime+",fail");
            logger.error("rightClick"+"失败");
            Assert.fail("rightClick"+"失败");
        }
    }

    public void clickRight(String locateWay, String locateValue, String locationElement) throws Exception {
        long startTime = new Date().getTime();
        String sttime = Long.toString(startTime);
        try {
            Actions actiondb = new Actions(driver);
            WebElement test1item = findView(locateWay, locateValue, locationElement);
            actiondb.contextClick(test1item).perform();
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " rightClick " + ++base.stepIndex + ","+sttime+","+sptime+",passed");
        }catch (Exception e){
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " rightClick " + ++base.stepIndex + ","+sttime+","+sptime+",fail");
            logger.error("clickRight"+"失败");
            Assert.fail("clickRight"+"失败");
        }

    }

    /**
     * 鼠标长按
     *
     * @param locateWay   定位方式
     * @param locateValue 定位值
     */
    public void longPress(String locateWay, String locateValue, String locationElement) throws Exception {
        long startTime = new Date().getTime();
        String sttime = Long.toString(startTime);
        try {
            Actions actiondb = new Actions(driver);
            WebElement test1item = findView(locateWay, locateValue, locationElement);
            actiondb.clickAndHold(test1item).perform();
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " longPress " + ++base.stepIndex + ","+sttime+","+sptime+",passed");

        }catch (Exception e){
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " longPress " + ++base.stepIndex + ","+sttime+","+sptime+",fail");
            logger.error("longPress"+"失败");
            Assert.fail("longPress"+"失败");
        }
    }

    // 移除某个属性值
    public void removeAttribute(String way, String value, String attribute) throws Exception {
        String js = "document.getElementById(\"" + value + "\").removeAttribute(\"" + attribute + "\");";
        ((JavascriptExecutor) driver).executeScript(js);
    }

    // fixme 需要左滑动和右滑动的js代码
    public void swipe(String direction) {
        if (direction == "UP") {
            String js = "window.scrollTo(0,document.body.scrollHeight)";
            ((JavascriptExecutor) driver).executeScript(js);

            this.saveScreenshotPost(base.getCurrentPage() + " swipe " + ++base.stepIndex);
        } else if (direction == "DOWN") {
            String js = "window.scrollTo(0,0)";
            ((JavascriptExecutor) driver).executeScript(js);

            this.saveScreenshotPost(base.getCurrentPage() + " swipe " + ++base.stepIndex);
        } else {
            logger.error("不存在的方向！");
        }
    }

    // fixme 聚焦到某个元素的js代码
    public void swipe(String locateWay, String locateValue, String locationElement, String direction) throws
            InterruptedException {
        if (direction == "ELE") {
            WebElement target = findView(locateWay, locateValue, locationElement);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", target);

            this.saveScreenshotPost(base.getCurrentPage() + " swipe " + ++base.stepIndex);
        } else {
            logger.error("不存在的方向！");
        }
    }

    public boolean isDecimal(String n) {
        return n.matches("\\d+\\.\\d+$");
    }

    public void drag_and_drop(String locateWay, String locateValue, String locationElement,
                              String locateWay1, String locateValue1, String locationElement1) throws Exception {
        long startTime = new Date().getTime();
        String sttime = Long.toString(startTime);
        try {
            if (locateWay.equals("XY") && locateWay1.equals("XY")) {
                String[] value1 = locateValue.split(",");
                int x1 = Integer.valueOf(value1[0]);
                int y1 = Integer.valueOf(value1[1]);
                String[] value2 = locateValue1.split(",");
                int x2 = Integer.valueOf(value2[0]);
                int y2 = Integer.valueOf(value2[1]);
                dropByXY(x1, y1, x2, y2);
                long stopTime = new Date().getTime();
                String sptime = Long.toString(stopTime);
                this.saveScreenshotPost(base.getCurrentPage() + " longPress " + ++base.stepIndex + "," + sttime + "," + sptime + ",passed");
            } else if (locateWay1.equals("XY")) {

                WebElement web_element_source = findView(locateWay, locateValue, locationElement);
                Point loction1 = web_element_source.getLocation();
                int x1 = loction1.x;
                int y1 = loction1.y;
                int x2;
                int y2;
                Long browser_navigation_panel_height = (Long) ((JavascriptExecutor) driver).executeScript("return window.outerHeight - window.innerHeight;");
                Long innerHeight = (Long) ((JavascriptExecutor) driver).executeScript("return window.innerHeight;");
                Long innerWidth = (Long) ((JavascriptExecutor) driver).executeScript("return window.innerWidth;");
                int totalRecord = browser_navigation_panel_height.intValue();
                String[] value2 = locateValue1.split(",");
                if (isDecimal(value2[0])) {
                    float x2temp = Float.parseFloat(value2[0]);
                    float y2temp = Float.parseFloat(value2[1]);
                    x2 = Math.round(x2temp * innerWidth);
                    y2 = Math.round(y2temp * innerHeight + totalRecord);
                } else {
                    int x2temp = Integer.valueOf(value2[0]);
                    int y2temp = Integer.valueOf(value2[1]);
                    x2 = x2temp;
                    y2 = y2temp + totalRecord;
                }
                y1 = y1 + totalRecord;
//            y2 = y2*innerHeight + totalRecord;
                dropByXY(x1, y1, x2, y2);
                long stopTime = new Date().getTime();
                String sptime = Long.toString(stopTime);
                this.saveScreenshotPost(base.getCurrentPage() + " drag_and_drop " + ++base.stepIndex + "," + sttime + "," + sptime + ",passed");
            } else {
                WebElement web_element_source = findView(locateWay, locateValue, locationElement);
                WebElement web_element_target = findView(locateWay1, locateValue1, locationElement1);
                Point loction1 = web_element_source.getLocation();
                Point loction2 = web_element_target.getLocation();
                int x1 = loction1.x;
                int y1 = loction1.y;
                int x2 = loction2.x;
                int y2 = loction2.y;
                Long browser_navigation_panel_height = (Long) ((JavascriptExecutor) driver).executeScript("return window.outerHeight - window.innerHeight;");
                int totalRecord = browser_navigation_panel_height.intValue();
                y1 = y1 + totalRecord;
                y2 = y2 + totalRecord;
                dropByXY(x1, y1, x2, y2);
                long stopTime = new Date().getTime();
                String sptime = Long.toString(stopTime);
                this.saveScreenshotPost(base.getCurrentPage() + " drag_and_drop " + ++base.stepIndex + "," + sttime + "," + sptime + ",passed");
            }
        }catch (Exception e){
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " drag_and_drop " + ++base.stepIndex + ","+sttime+","+sptime+",fail");
            logger.error("drag_and_drop"+"失败");
            Assert.fail("drag_and_drop"+"失败");
        }
    }

    private void dropByXY(int x1, int y1,int x2,int y2) throws Exception {
        Robot robot = new Robot();
        robot.mouseMove(x1, y1);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        Thread.sleep(200);
        robot.mouseMove(x2,y2);
        Thread.sleep(200);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);

    }

    public void sleep(int second) throws InterruptedException {
        if(second % 1000 == 0) {
            Thread.sleep(second);
        }else Thread.sleep(second*1000);
    }

    /**
     * 切换至frame,
     *
     * @param name frame id name
     */
    public void frame(String name) {
        long startTime = new Date().getTime();
        String sttime = Long.toString(startTime);
        try {


            driver.switchTo().frame(name);
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " frame " + ++base.stepIndex + ","+sttime+","+sptime+",passed");

        }catch (Exception e){
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " frame " + ++base.stepIndex + ","+sttime+","+sptime+",fail");
            logger.error("frame"+"失败");
            Assert.fail("frame"+"失败");
        }
    }

    /**
     * 切换至frame,
     *
     * @param index frame的 index
     */
    public void frame(int index) {
        long startTime = new Date().getTime();
        String sttime = Long.toString(startTime);
        try {
            driver.switchTo().frame(index);
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " drag_and_drop " + ++base.stepIndex + ","+sttime+","+sptime+",passed");
        }catch (Exception e){
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " drag_and_drop " + ++base.stepIndex + ","+sttime+","+sptime+",fail");
            logger.error("frame"+"失败");
            Assert.fail("frame"+"失败");
        }
    }

    /**
     * 切换至frame,
     *
     * @param locateWay locateValue
     */
    public void frame(String locateWay, String locateValue, String locationElement) throws InterruptedException {
        long startTime = new Date().getTime();
        String sttime = Long.toString(startTime);
        try {
            WebElement frame = findView(locateWay, locateValue, locationElement);
            driver.switchTo().frame(frame);
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " frame " + ++base.stepIndex + ","+sttime+","+sptime+",passed");
        }catch (Exception e){
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " frame " + ++base.stepIndex + ","+sttime+","+sptime+",fail");
            logger.error("frame"+"失败");
            Assert.fail("frame"+"失败");
        }
    }

    /**
     * 切换至frame,再切换回主文档
     */
    public void frame2Default() {
        long startTime = new Date().getTime();
        String sttime = Long.toString(startTime);
        try {
            driver.switchTo().defaultContent();
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " frame2Default " + ++base.stepIndex + ","+sttime+","+sptime+",passed");
        }catch (Exception e){
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " frame2Default " + ++base.stepIndex + ","+sttime+","+sptime+",fail");
            logger.error("frame"+"失败");
            Assert.fail("frame"+"失败");
        }
    }

    /**
     * 切换至最新的窗口句柄
     */
    public void window() throws InterruptedException {
        long startTime = new Date().getTime();
        String sttime = Long.toString(startTime);
        try {
            sleep(3000);
            Set<String> handles = driver.getWindowHandles();
            System.out.println(handles);
            Iterator<String> i = handles.iterator();
            while (i.hasNext()) {
                driver.switchTo().window(i.next());
            }
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " window " + ++base.stepIndex + ","+sttime+","+sptime+",passed");
        }catch (Exception e){
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " window " + ++base.stepIndex + ","+sttime+","+sptime+",fail");
            logger.error("frame"+"失败");
            Assert.fail("frame"+"失败");
        }
    }

    /**
     * 通过Title来切换窗口
     */
    public void window(String windowTitle) {
        long startTime = new Date().getTime();
        String sttime = Long.toString(startTime);
        try{
        boolean flag = false;
        String currentHandle = driver.getWindowHandle();
        Set<String> handles = driver.getWindowHandles();
        for (String s : handles) {
            // if (s.equals(currentHandle))
            // continue;
            // else {
            driver.switchTo().window(s);
            if (driver.getTitle().contains(windowTitle)) {
                flag = true;
                System.out.println("Switch to window: " + windowTitle + " successfully!");
                break;
            } else
                continue;
            // }
        }
        if (flag == false) {
            System.out.printf("Window: " + windowTitle + " cound not found!");
        }
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " window " + ++base.stepIndex + ","+sttime+","+sptime+",passed");
        }catch(Exception e){
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " window " + ++base.stepIndex + ","+sttime+","+sptime+",fail");
            logger.error("window"+"失败");
            Assert.fail("window"+"失败");
        }
    }

    /**
     * 获取alert值
     */
    public String switch2AlertAndGetText() {
        long startTime = new Date().getTime();
        String sttime = Long.toString(startTime);
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " switch2AlertAndGetText " + ++base.stepIndex + ","+sttime+","+sptime+",passed");
            return alertText;
        } finally {
            acceptNextAlert = true;

        }
    }

    /**
     * accept alert
     */
    public void switch2Alert() {
        long startTime = new Date().getTime();
        String sttime = Long.toString(startTime);

        try {
            Alert alert = driver.switchTo().alert();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }

        } finally {
            acceptNextAlert = true;
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " switch2Alert " + ++base.stepIndex + ","+sttime+","+sptime+",passed");

        }
    }

    /**
     * alert输入值，并接受
     */
    public void switch2AlertSendkeys(String value) {
        try {
            Alert alert = driver.switchTo().alert();
            alert.sendKeys(value);
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
        } finally {
            acceptNextAlert = true;
            this.saveScreenshotPost(base.getCurrentPage() + " switch2AlertSendkeys " + ++base.stepIndex);
        }
    }

    public void mouseOver(String way, String value, String locationElement) throws InterruptedException {
        long startTime = new Date().getTime();
        String sttime = Long.toString(startTime);
        try {
            WebElement target = findView(way, value, locationElement);
            Actions actiondb = new Actions(driver);
            actiondb.clickAndHold(target).perform();
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " mouseOver " + ++base.stepIndex + ","+sttime+","+sptime+",fail");
        }catch (Exception e){
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " mouseOver " + ++base.stepIndex + ","+sttime+","+sptime+",fail");
            logger.error("window"+"失败");
            Assert.fail("window"+"失败");
        }
    }

    public void jsexecut(String js, Object... args) {
        if (js == null) {
            System.out.println("js为空");
        } else {
            ((JavascriptExecutor) driver).executeScript(js, args);
            this.saveScreenshotPost(base.getCurrentPage() + " jsexecut " + ++base.stepIndex);
        }
    }

    /**
     * click并截图
     */
    public void clickWithscreenshot(String way, String value, String locationElement) throws Exception {
        long startTime = new Date().getTime();
        String sttime = Long.toString(startTime);
        try {
            WebElement click = findView(way, value, locationElement);
            click.click();
            saveScreenshotPNG();
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " clickWithscreenshot " + ++base.stepIndex + ","+sttime+","+sptime+",passed");
        }catch (Exception e){
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " clickWithscreenshot " + ++base.stepIndex + ","+sttime+","+sptime+",fail");
            logger.error("clickWithscreenshot"+"失败");
            Assert.fail("clickWithscreenshot"+"失败");
        }
    }

    @Attachment(value = "Page screenshot", type = "image/png")
    public byte[] saveScreenshotPNG() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    public void saveScreenshotPost(String name) {
        if (stepScreenShot.equals("1")) {
            Allure.addAttachment(name, "image/png", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)), "png");
        }
    }

    /**
     * 截图
     */
    public String screenShotwithoutname() {
        try {
            // 生成时间戳
            String dateString = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_").format(new Date());
            File file3 = new File("");
            System.out.println("file4:" + this.getClass().getResource("/").getPath());

            //String dir_name = System.getProperty("user.dir") +  "/Screenshot" ;
            String dir_name = getProjectRootDir() + "/Screenshot";
            System.out.println("图片目录:" + dir_name);

            File dir = new File(dir_name);
            // 由于可能存在异常图片的且当被删除的可能，所以这边先判断目录是否存在
            if (!(dir.isDirectory())) {
                // 判断是否存在该目录
                dir.mkdir();
            }
            // 调用方法捕捉画面
            File screen = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            // 复制文件到指定目录
            // 图片最后存放的路径由 目录：dir_name +时间戳+测试套件+测试用例+测试步骤组合生成

            String imgPath = dir_name + "/" + dateString + ".png";
            FileUtils.copyFile(screen, new File(imgPath));

            return imgPath;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("截图失败");
        }
    }

    /**
     * 复选框单选
     */
    public void selectOneCheckBox(String way, String value, String locationElement) throws InterruptedException {
        findView(way, value, locationElement).click();
    }

    /**
     * 复选框全选
     */
    public void selectAllCheckBox(String way, String value) {
        List<WebElement> li = findViews(way, value);
        for (int i = 0; i < li.size(); i++) {
            li.get(i).click();
        }
    }

    /**
     * 按下标选择下拉选项
     */
    public void selectSelectByIndex(String way, String value, String locationElement, int index) throws
            InterruptedException {
        WebElement el = findView(way, value, locationElement);
        new Select(el).selectByIndex(index);
        this.saveScreenshotPost(base.getCurrentPage() + " selectSelectByIndex " + ++base.stepIndex);
    }

    /**
     * 按可见文本选择下拉选项
     */
    public void selectSelectByVisibleText(String way, String value, String locationElement, String text) throws
            InterruptedException {
        WebElement el = findView(way, value, locationElement);
        new Select(el).selectByVisibleText(text);
        this.saveScreenshotPost(base.getCurrentPage() + " selectSelectByVisibleText " + ++base.stepIndex);
    }

    /**
     * 按value选择下拉选项
     */
    public void selectSelectByValue(String way, String value, String locationElement, String value1) throws
            InterruptedException {
        WebElement el = findView(way, value, locationElement);
        new Select(el).selectByValue(value1);
        this.saveScreenshotPost(base.getCurrentPage() + " selectSelectByValue " + ++base.stepIndex);
    }

    /**
     * * @Description: 连接mysql数据库
     *
     * @return Connection 连接数据的对象
     * @author xujun
     */
    public static Connection getConnection() {

        try {

            Class.forName("com.mysql.jdbc.Driver"); // 加载驱动

            System.out.println("加载驱动成功!!!");
        } catch (ClassNotFoundException e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        try {

            // 通过DriverManager类的getConenction方法指定三个参数,连接数据库
            Conn = DriverManager.getConnection(URL, UserName, Password);
            System.out.println("连接数据库成功!!!");

            // 返回连接对象
            return Conn;

        } catch (SQLException e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
    }

    /**
     * * @Description:执行 sql 获取执行结果
     *
     * @param sql sql语句
     * @return String 执行结果
     * @author xujun
     */
    public String mysql_search(String sql) {
        String result = null;
        try {
            // 获取连接
            conn = getConnection();
            if (conn == null)
                return result;
            // 定义sql语句
            String Sql = sql;
            // 执行sql语句
            stt = conn.createStatement();
            // 返回结果集
            set = stt.executeQuery(Sql);
            // 获取数据
            while (set.next()) {
                result = set.getString(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            // 释放资源
            try {
                set.close();
                conn.close();
            } catch (Exception e2) {
                // TODO: handle exception
            }
        }
        return result;
    }

    /**
     * * @Description: 获取sql的执行结果和元素的实际值进行相等的比较
     *
     * @param locateWay   定位方式
     * @param locateValue 定位值
     * @param sql         sql语句
     * @author xujun
     */
    public void assertDtEqualsDB(String locateWay, String locateValue, String locationElement, String sql) throws
            Exception {
        String actual = findView(locateWay, locateValue, locationElement).getText();
        String expect = mysql_search(sql);
        try {
            Assert.assertEquals(actual, expect);
            logger.error("实际值：" + actual + " ; DB查询值：" + expect);
        } catch (AssertionError e) {
            logger.error("实际值：" + actual + " ; DB查询值：" + expect);
            Assert.fail("实际值：" + actual + " ; DB查询值：" + expect);
            throw e;
        }

    }

    /**
     * * @Description: 获取sql的执行结果和元素的实际值进行不等于的断言
     *
     * @param locateWay   定位方式
     * @param locateValue 定位值
     * @param sql         sql语句
     * @author xujun
     */
    public void assertDtNotEqualsDB(String locateWay, String locateValue, String locationElement, String sql) throws
            Exception {
        String actual = findView(locateWay, locateValue, locationElement).getText();
        String expect = mysql_search(sql);
        try {
            Assert.assertNotEquals(actual, expect);
            logger.error("实际值：" + actual + " ; 期望值：" + expect);
        } catch (AssertionError e) {
            logger.error("实际值：" + actual + " ; 期望值：" + expect);
            Assert.fail("实际值：" + actual + " ; 期望值：" + expect);
            throw e;
        }

    }

    /**
     * * @Description: 获取sql的执行结果和元素的实际值进行包含的断言
     *
     * @param locateWay   定位方式
     * @param locateValue 定位值
     * @param sql         sql语句
     * @author xujun
     */
    public void assertDtContainDB(String locateWay, String locateValue, String locationElement, String sql) throws
            Exception {
        String actual = findView(locateWay, locateValue, locationElement).getText();
        String expect = mysql_search(sql);
        try {
            assert (actual.contains(expect));
            logger.error("实际值：" + actual + " ; 期望值：" + expect);
        } catch (AssertionError e) {
            logger.error("实际值：" + actual + " ; 期望值：" + expect);
            Assert.fail("实际值：" + actual + " ; 期望值：" + expect);
            throw e;
        }

    }

    public String getVerificationcode(String ip, String key, int type, String phone) throws Exception {
        String verification = null;
        // 连接redis
        Jedis jedis = new Jedis(ip, 6379);
        System.out.println("connect redis seccessful!");
        // 获取数据
        Map<String, String> userMap = jedis.hgetAll(key);
        for (Map.Entry<String, String> item : userMap.entrySet()) {
            String codekey = item.getKey().substring(1, 14);
            String actualkey = type + "_" + phone;
            if (actualkey.equals(codekey)) {
                String v1 = item.getValue();
                String verifi = v1.substring(v1.length() - 7, v1.length() - 1);
                verification = verifi;
            }
        }
        // 释放资源
        jedis.close();
        return verification;
    }

    public void ifclick(String locateWay, String locateValue, String locationElement) {
        try {
            click(locateWay, locateValue, locationElement);
        } catch (NoSuchElementException e) {
            throw e;
        }
    }

    public void clickByImage(String path) {
        try {
            int[] a = match_pt("IMAGE",path);
            move_By_XY(a[0], a[1]);
            logger.info("通过" + "IMAGE" + "定位到" + path + "并点击");
        } catch (AWTException  e) {
            e.printStackTrace();
            logger.error("未通过" + "IMAGE" + "定位到" + path);
        }
    }

    public int[] picture_xy(String path) throws IOException, AWTException {

        String inputImg = getProjectRootDir() + "/images/element/" + path;
        String baseImg = screen_for_IMAGE();

        Mat baseTemplate = Imgcodecs.imread(baseImg);
        Mat inputMat = Imgcodecs.imread(inputImg);
        int w = inputMat.cols();
        int h = inputMat.rows();
        Mat result = Mat.zeros(baseTemplate.rows() - inputMat.rows() + 1, baseTemplate.cols() - inputMat.cols() + 1, CvType.CV_32FC1);
        Imgproc.matchTemplate(baseTemplate, inputMat, result, Imgproc.TM_CCOEFF);
        boolean exist = false;
        for (int i = 0; i < result.rows(); i++) {
            for (int j = 0; j < result.cols(); j++) {
                if (result.get(i, j)[0] >= thresholdvalue) {
                    exist = true;
                    break;
                }
            }
            if (exist) {
                break;
            }
        }
        int v_x = 0;
        int v_y = 0;

        if (exist) {
            System.out.println("匹配成功");
            int[] aa = templete(0, inputImg, baseImg);
            v_x = aa[0];
            v_y = aa[1];
            System.out.println(v_x + "," + v_y);

        } else {

            System.out.println("匹配失败");
            throw new TimeoutException("定位失败");
        }
        return new int[]{v_x, v_y};
    }


    public int[] templete(int method, String de, String temp) {
        Mat templete = Imgcodecs.imread(temp);
        Mat demo = Imgcodecs.imread(de);
        int w = demo.cols();
        int h = demo.rows();
        int width = templete.cols() - demo.cols() + 1;
        int height = templete.rows() - demo.rows() + 1;
        Mat result = new Mat(width, height, CvType.CV_32FC1);
        Imgproc.matchTemplate(templete, demo, result, 1);
        Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());
        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
        double x, y;
        if (method == Imgproc.TM_SQDIFF_NORMED || method == Imgproc.TM_SQDIFF) {
            x = mmr.minLoc.x;
            y = mmr.minLoc.y;
        } else {
            x = mmr.maxLoc.x;
            y = mmr.maxLoc.y;
        }
        int m = (int) x;
        int n = (int) y;
        int f = (m + w / 2);
        int g = (n + h / 2);
        return new int[]{f, g};
    }


    public void intelligentlyWait(String path) throws InterruptedException, IOException, AWTException {
        int i = 0;
        while (true) {
            String inputImg = getProjectRootDir() + "/images/node/" + path;

            String baseImg = screen_for_IMAGE();

            double compareHist = compare_image(inputImg, baseImg);
            System.out.println(1 - compareHist);
            if (1 - compareHist > thresholdvalue) {
                System.out.println("图片匹配");
                logger.info("图片匹配,智能等待通过");

                this.saveScreenshotPost(base.getCurrentPage() + " intelligentlyWait " + ++base.stepIndex);
                break;
            } else {
                i += 1;
                if (i == 3) {
                    System.out.println("图片不匹配");


                    logger.error("图片不匹配，智能等待失败：" + inputImg);
                    throw new TimeoutException("智能等待超时");


                } else {
                    Thread.sleep(5000);
                }
            }
        }
    }

    public double compare_image(String img_1, String img_2) {

        Mat image0 = Imgcodecs.imread(img_1);
        Mat image1 = Imgcodecs.imread(img_2);
        Mat hist0 = new Mat();
        Mat hist1 = new Mat();

        MatOfFloat ranges = new MatOfFloat(0f, 256f);
        MatOfInt histSize = new MatOfInt(1000);

        Imgproc.calcHist(Arrays.asList(image0), new MatOfInt(0), new Mat(), hist0, histSize, ranges);
        Imgproc.calcHist(Arrays.asList(image1), new MatOfInt(0), new Mat(), hist1, histSize, ranges);
        double res = Imgproc.compareHist(hist0, hist1, Imgproc.CV_COMP_BHATTACHARYYA);
        return res;
    }

    public String screen_for_IMAGE() throws AWTException, IOException {
        Robot robot = new Robot();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle screenRect = new Rectangle(d);
        System.out.println(d);
        BufferedImage bufferedImage = robot.createScreenCapture(screenRect);
        String dir_name = getProjectRootDir() + "/Screenshot";
        File dir = new File(dir_name);
        // 由于可能存在异常图片的且当被删除的可能，所以这边先判断目录是否存在
        if (!(dir.isDirectory())) {
            // 判断是否存在该目录
            dir.mkdir();
        }
        String dateString = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_").format(new Date());

        String imgPath = dir_name + "/" + dateString + ".png";
        File file = new File(imgPath);

        ImageIO.write(bufferedImage, "png", file);
        return imgPath;
    }

    public void move_By_XY(int x, int y) throws AWTException {
        Robot robot = new Robot();
        robot.mouseMove(x, y);
        System.out.println("单击");
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        logger.info("通过" + "(" + x + "," + y + ")" + "点击");
    }

    public boolean Region_picture_assertion_alone(String path) throws
            IOException, AWTException, InterruptedException {

        String inputImg = getProjectRootDir() + "/images/element/" + path;
        String[] baseImg = screen_for_IMAGE_R();
        Mat baseTemplate = Imgcodecs.imread(baseImg[0]);
        Mat inputMat = Imgcodecs.imread(inputImg);
        Mat result = Mat.zeros(baseTemplate.rows() - inputMat.rows() + 1, baseTemplate.cols() - inputMat.cols() + 1, CvType.CV_32FC1);
        Imgproc.matchTemplate(baseTemplate, inputMat, result, Imgproc.TM_CCOEFF);
        boolean exist = false;
        int m = 0;
        while (m <= 2) {
            for (int i = 0; i < result.rows(); i++) {
                for (int j = 0; j < result.cols(); j++) {
                    if (result.get(i, j)[0] >= 0.95) {
                        exist = true;
                        break;
                    }
                }
                if (exist) {
                    break;
                }
            }
            m += 1;
            if (exist) {
                break;
            } else {
                sleep(500);

            }
        }
        int x1 = 0;
        int y1 = 0;
        int x2 = 0;
        int y2 = 0;
        if (exist) {
            int o = 0;
            while (true) {
                int[] aa_2 = templete_R(0, inputImg, baseImg[0]);
                x1 = aa_2[0];
                y1 = aa_2[1];
                x2 = aa_2[2];
                y2 = aa_2[3];
                cut(baseImg[0], aa_2[0], aa_2[1], aa_2[2], aa_2[3], baseImg[1]);
                boolean mm = intelligentlyWait_R(baseImg[1], inputImg);
                if (mm) {
                    break;
                } else {
                    if (o == 2) {
                        return false;
                    } else {
                        sleep(500);
                    }
                }
                o += 1;
            }
        }

        return true;


    }

    public void intelligentlyWaitByRegion(String way, String path, String name) throws
            InterruptedException, IOException, AWTException {
        if (way.equals("IMAGE")) {
            try {
                int i = 0;
                while (true) {
                    if (i == 3) {
                        logger.error("图片不匹配，智能等待失败：" + path);
                        throw new TimeoutException("智能等待超时");

                    }
                    boolean m = Region_picture_assertion_alone(path);
                    if (m == true) {
                        logger.info("图片匹配,智能等待通过");
                        this.saveScreenshotPost(base.getCurrentPage() + " intelligentlyWaitByRegion " + ++base.stepIndex);
                        break;
                    }
                    i += 1;
                    sleep(2000);

                }
            } catch (AssertionError m) {
                logger.error("断言元素存在");
                throw m;
            }
        } else if (way.equals("DOUBLE_IMAGE")) {
            try {
                String[] array = path.split(",");
                String b = array[1];
                String a = array[0];
                int i = 0;
                while (true) {
                    if (i == 3) {
                        logger.error("图片不匹配，智能等待失败：" + path);
                        throw new TimeoutException("智能等待超时");

                    }
                    boolean m = Region_picture_assertion_double(b, a);
                    if (m == true) {
                        logger.info("图片匹配,智能等待通过");
                        break;
                    }
                    i += 1;
                    sleep(2000);
                }


            } catch (AssertionError m) {
                logger.error("断言元素存在");
                throw m;
            }
        }
    }

    public String[] screen_for_IMAGE_R() throws AWTException, IOException {
        Robot robot = new Robot();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle screenRect = new Rectangle(d);
        System.out.println(d);
        BufferedImage bufferedImage = robot.createScreenCapture(screenRect);
        String dir_name = getProjectRootDir() + "/Screenshot";
        File dir = new File(dir_name);
        // 由于可能存在异常图片的且当被删除的可能，所以这边先判断目录是否存在
        if (!(dir.isDirectory())) {
            // 判断是否存在该目录
            dir.mkdir();
        }
        String dateString = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_").format(new Date());

        String imgPath = dir_name + "/" + dateString + ".png";
        String imgPath_1 = dir_name + "/" + dateString + "_666" + ".png";
        String imgPath_2 = dir_name + "/" + dateString + "_777" + ".png";
        File file = new File(imgPath);

        ImageIO.write(bufferedImage, "png", file);
        return new String[]{imgPath, imgPath_1, imgPath_2};
    }

    public boolean Region_picture_assertion_double(String path1, String path2) throws
            IOException, AWTException, InterruptedException {

        String inputImg = getProjectRootDir() + "/images/element/" + path1;
        String[] baseImg_R = screen_for_IMAGE_R();
        String baseImg = baseImg_R[0];
        Mat baseTemplate = Imgcodecs.imread(baseImg);
        Mat inputMat = Imgcodecs.imread(inputImg);
        int w = inputMat.cols();
        int h = inputMat.rows();
        Mat result = Mat.zeros(baseTemplate.rows() - inputMat.rows() + 1, baseTemplate.cols() - inputMat.cols() + 1, CvType.CV_32FC1);
        Imgproc.matchTemplate(baseTemplate, inputMat, result, Imgproc.TM_CCOEFF);
        boolean exist = false;
        for (int i = 0; i < result.rows(); i++) {
            for (int j = 0; j < result.cols(); j++) {
                if (result.get(i, j)[0] >= 0.9) {

                    exist = true;
                    break;
                }
            }
            if (exist) {
                break;
            }
        }
        int x1 = 0;
        int y1 = 0;
        int x2 = 0;
        int y2 = 0;

        if (exist) {
            int o = 0;
            while (true) {
                int[] aa_2 = templete_R(0, inputImg, baseImg);
                x1 = aa_2[0];
                y1 = aa_2[1];
                x2 = aa_2[2];
                y2 = aa_2[3];
                cut(baseImg, aa_2[0], aa_2[1], aa_2[2], aa_2[3], baseImg_R[1]);
                boolean mm = intelligentlyWait_R(baseImg_R[1], inputImg);
                if (mm) {
                    break;
                } else {
                    if (o == 2) {

                        logger.info(path1 + "图片断言不匹配");
                        return false;
                    } else {
                        sleep(500);
                    }

                }
                o += 1;
            }
            String inputImg_2 = getProjectRootDir() + "/images/element/" + path2;
            String baseImg_2 = baseImg_R[1];

            Mat baseTemplate_2 = Imgcodecs.imread(baseImg_2);
            Mat inputMat_2 = Imgcodecs.imread(inputImg_2);
            int w_2 = inputMat.cols();
            int h_2 = inputMat.rows();
            Mat result_2 = Mat.zeros(baseTemplate_2.rows() - inputMat_2.rows() + 1, baseTemplate_2.cols() - inputMat_2.cols() + 1, CvType.CV_32FC1);
            Imgproc.matchTemplate(baseTemplate_2, inputMat_2, result_2, Imgproc.TM_CCOEFF);
            boolean exist_2 = false;
            for (int i_2 = 0; i_2 < result_2.rows(); i_2++) {
                for (int j_2 = 0; j_2 < result_2.cols(); j_2++) {
                    if (result_2.get(i_2, j_2)[0] >= 0.98) {
                        System.out.println(result_2.get(i_2, j_2)[0]);
                        exist_2 = true;
                        break;
                    }
                }
                if (exist_2) {
                    break;
                }
            }
            int v_x_2 = 0;
            int v_y_2 = 0;

            if (exist_2) {
                while (true) {
                    int[] aa_3 = templete_R(0, inputImg_2, baseImg_2);
                    x1 = aa_3[0];
                    y1 = aa_3[1];
                    x2 = aa_3[2];
                    y2 = aa_3[3];
                    cut(baseImg_2, x1, y1, x2, y2, baseImg_R[2]);
                    boolean mm = intelligentlyWait_R(baseImg_R[2], inputImg_2);
                    if (mm) {
                        break;
                    } else {
                        if (o == 2) {

                            logger.info(path2 + "图片断言不匹配");
                            return false;
                        } else {
                            sleep(500);
                        }

                    }
                    o += 1;
                }
            }

        }
        logger.info(path2 + "图片断言成功");
        return true;
    }

    public int[] templete_R(int method, String de, String temp) {
        Mat templete = Imgcodecs.imread(temp);
        Mat demo = Imgcodecs.imread(de);
        int w = demo.cols();
        int h = demo.rows();
        int width = templete.cols() - demo.cols() + 1;
        int height = templete.rows() - demo.rows() + 1;
        Mat result = new Mat(width, height, CvType.CV_32FC1);
        Imgproc.matchTemplate(templete, demo, result, 1);
        Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());
        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
        double x, y;
        if (method == Imgproc.TM_SQDIFF_NORMED || method == Imgproc.TM_SQDIFF) {
            x = mmr.minLoc.x;
            y = mmr.minLoc.y;
        } else {
            x = mmr.maxLoc.x;
            y = mmr.maxLoc.y;
        }
        int m = (int) x;
        int n = (int) y;
        int f = w;
        int g = h;

        return new int[]{m, n, f, g};
    }

    public void clickPcKeyboard(String key) {
        if (key.equals("f1")) {
            Actions action = new Actions(driver);
            action.sendKeys(Keys.F1).perform();
            logger.info("按下F1");
        } else if (key.equals("f4")) {
            Actions action = new Actions(driver);
            action.sendKeys(Keys.F4).perform();
            logger.info("按下F4");
        } else if (key.equals("pgup")) {
            Actions action = new Actions(driver);
            action.sendKeys(Keys.PAGE_UP).perform();
            logger.info("按下Pg up");
        } else if (key.equals("pgdn")) {
            Actions action = new Actions(driver);
            action.sendKeys(Keys.PAGE_DOWN).perform();
            logger.info("按下Pg dn");
        } else if (key.equals("down")) {
            Actions action = new Actions(driver);
            action.sendKeys(Keys.DOWN).perform();
            logger.info("按下下键");
        } else if (key.equals("up")) {
            Actions action = new Actions(driver);
            action.sendKeys(Keys.UP).perform();
            logger.info("按下上键");
        } else if (key.equals("left")) {
            Actions action = new Actions(driver);
            action.sendKeys(Keys.LEFT).perform();
            logger.info("按下左键");
        } else if (key.equals("right")) {
            Actions action = new Actions(driver);
            action.sendKeys(Keys.RIGHT).perform();
            logger.info("按下右键");
        } else if (key.equals("+")) {
            Actions action = new Actions(driver);
            action.sendKeys("+").perform();
            logger.info("按下+");
        } else if (key.equals("-")) {
            Actions action = new Actions(driver);
            action.sendKeys("-").perform();
            logger.info("按下-");
        } else if (key.equals("*")) {
            Actions action = new Actions(driver);
            action.sendKeys("*").perform();
            logger.info("按下*");
        } else if (key.equals("alt7")) {
            Actions action = new Actions(driver);
            action.keyDown(Keys.ALT).keyDown(Keys.NUMPAD7).keyUp(Keys.ALT).keyUp(Keys.NUMPAD7).perform();
            logger.info("按下Alt+7");
        } else if (key.equals("cr8")) {
            Actions action = new Actions(driver);
            action.keyDown(Keys.CONTROL).keyDown(Keys.NUMPAD8).keyUp(Keys.CONTROL).keyUp(Keys.NUMPAD8).perform();
            logger.info("按下Ctrl+8");
        } else if (key.equals("enter")) {
            Actions action = new Actions(driver);
            action.sendKeys(Keys.ENTER).perform();
            logger.info("按下Enter");
        } else if (key.length() == 1) {
            Actions action = new Actions(driver);
            action.sendKeys(key).perform();
            logger.info("按下" + key);
        } else {
            logger.error("选择的按键" + key + "不在框架中");
            Assert.fail("选择的按键" + key + "不在框架中");
        }


        this.saveScreenshotPost(base.getCurrentPage() + " carriageReturn " + ++base.stepIndex);
    }

    public String getCookie() {
        Set<Cookie> cookie = driver.manage().getCookies();
        return cookie.toString();
    }

    public boolean intelligentlyWait_R(String path1, String path2) throws InterruptedException {
        int i = 0;
        while (true) {
            String inputImg = path1;

            String baseImg = path2;

            double compareHist = compare_image(inputImg, baseImg);
            System.out.println(1 - compareHist);

            if (1 - compareHist > 0.85) {
                return true;

            } else {
                return false;
            }
        }
    }

    public void cut(String srcpath, int x, int y, int width, int height, String subpath) throws IOException {

        FileInputStream is = null;
        ImageInputStream iis = null;

        try {

            is = new FileInputStream(srcpath);
            Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName("png");
            ImageReader reader = it.next();
            iis = ImageIO.createImageInputStream(is);
            reader.setInput(iis, true);
            ImageReadParam param = reader.getDefaultReadParam();
            Rectangle rect = new Rectangle(x, y, width, height);
            param.setSourceRegion(rect);
            BufferedImage bi = reader.read(0, param);
            ImageIO.write(bi, "png", new File(subpath));
        } finally {
            if (is != null)
                is.close();
            if (iis != null)
                iis.close();
        }
    }

    public void gotoUrl(String url) {
        long startTime = new Date().getTime();
        String sttime = Long.toString(startTime);
        try {
            driver.get(url);
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " gotoUrl " + ++base.stepIndex + ","+sttime+","+sptime+",passed");
        }catch (Exception e){
            long stopTime = new Date().getTime();
            String sptime = Long.toString(stopTime);
            this.saveScreenshotPost(base.getCurrentPage() + " gotoUrl " + ++base.stepIndex + ","+sttime+","+sptime+",fail");
            logger.error("gotoUrl"+"失败");
            Assert.fail("gotoUrl"+"失败");
        }
    }


    // if事件相等
    public boolean ifDtEquals(String locateWay, String locateValue, String locationElement, String expect) throws
            Exception {
        String actual = findView(locateWay, locateValue, locationElement).getText();
        // assert actual.equals(expect);
        return actual.equals(expect);
    }

    /**
     * if条件包含
     *
     * @param locateWay
     * @param locateValue
     * @param expect
     * @throws Exception
     */
    public boolean ifDtContain(String locateWay, String locateValue, String locationElement, String expect) throws
            Exception {
        String actual = findView(locateWay, locateValue, locationElement).getText();
        return actual.contains(expect);
    }


    /**
     * if条件不相等
     *
     * @param locateWay
     * @param locateValue
     * @param expect
     * @throws Exception
     */
    public boolean ifNotEquals(String locateWay, String locateValue, String locationElement, String expect) throws
            InterruptedException {
        String actual = findView(locateWay, locateValue, locationElement).getText();
        return !actual.equals(expect);
    }


    /**
     * if 元素是否被选中
     *
     * @param way
     * @param value
     * @throws Exception
     */
    public boolean ifSelected(String way, String value, String locationElement) throws InterruptedException {
        return findView(way, value, locationElement).isSelected();
    }

    /**
     * if条件元素未被选中
     *
     * @param way
     * @param value
     * @throws Exception
     */
    public boolean ifNotSelected(String way, String value, String locationElement) throws InterruptedException {
        return !findView(way, value, locationElement).isSelected();
    }

    public boolean ifDisplayed(String way, String value, String locationElement) throws InterruptedException {
        return findView(way, value, locationElement).isDisplayed();
    }

    public boolean ifNotDisplayed(String way, String value, String locationElement) {
        return !isElementExist(way, value, locationElement);
    }

    public boolean ifEnabled(String way, String value, String locationElement) throws InterruptedException {
        return findView(way, value, locationElement).isEnabled();
    }

    public boolean ifNotEnabled(String way, String value, String locationElement) throws Exception {
        return !findView(way, value, locationElement).isEnabled();
    }

    public boolean ifAttributeEqual(String way, String value, String locationElement, String attribute, String
            except) throws Exception {
        String actual = findView(way, value, locationElement).getAttribute(attribute);
        return actual.equals(except);
    }

    public boolean ifTitle(String title) {
        String actual = driver.getTitle();
        return actual.equals(title);
    }

    public boolean ifTitleContains(String title) {
        String actual = driver.getTitle();
        return actual.contains(title);
    }

    /**
     * 断言alert的值是否与期望相等
     *
     * @param expect
     * @throws Exception
     */
    public boolean ifAlertMessage(String expect) throws Exception {
        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        alert.accept();
        return alertText.equals(expect);
    }


    public boolean ifAlertPresent() {
        return isAlertPresent();
    }


    private String startServer(String filename, String params) {
        Socket socket = null;
        try {
            InetAddress addr = InetAddress.getLocalHost();
            String host = addr.getHostName();
            //String ip=addr.getHostAddress().toString(); //获取本机ip
            //log.info("调用远程接口:host=>"+ip+",port=>"+12345);

            // 初始化套接字，设置访问服务的主机和进程端口号，HOST是访问python进程的主机名称，可以是IP地址或者域名，PORT是python进程绑定的端口号
            socket = new Socket(host, 8999);

            // 获取输出流对象
            OutputStream os = socket.getOutputStream();
            PrintStream out = new PrintStream(os);
            // 发送内容
            out.print(filename + "|" + params);
            // 告诉服务进程，内容发送完毕，可以开始处理
            out.print("over");
            // 获取服务进程的输入流
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String tmp = null;
            StringBuilder sb = new StringBuilder();
            // 读取内容
            while ((tmp = br.readLine()) != null)
                sb.append(tmp).append('\n');

            return sb.toString();
            // 解析结果
            //JSONArray res = JSON.parseArray(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) socket.close();
            } catch (IOException e) {
            }
            System.out.println("远程接口调用结束.");
        }
        return "";
    }

    static class MyClassLoader extends URLClassLoader {
        public MyClassLoader(URL[] urls) {
            super(urls);
        }

        public MyClassLoader(URL[] urls, ClassLoader parent) {
            super(urls, parent);
        }

        public void addJar(URL url) {
            this.addURL(url);
        }
    }

    public int[] match_pt(String locateWay, String locateValue) {
        Process proc;
        int pp[] = {-1,-1};
        try {
            String template = "";
            String img = screen_for_IMAGE();
            String temImg = "";
            if(locateWay.equalsIgnoreCase("IMAGE")) {
                template = getProjectRootDir() + "/images/element/" + locateValue;
                temImg = img;
            } else {
                String[] array = locateValue.split(",");
                String b = array[1];
                String a = array[0];
                template = getProjectRootDir() + "/images/element/" + a;
                temImg = getProjectRootDir() + "/images/element/" + b;
            }

//            String template = "F:\\project\\xiamen10086\\testproject5\\android_template\\template.png";

            String[] argss = new String[] { "python", "./src/main/java/utilities/PicMatch.py", template,img,temImg };
//            String[] argss = new String[] { "python", "F:\\project\\xiamen10086\\testproject5\\android_template\\src\\test\\java\\testcase\\PicMatch.py", a };

            proc = Runtime.getRuntime().exec(argss);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            int i=0;

            while ((line = in.readLine()) != null) {
                if(!line.contains("error")) {
                    int x = Integer.parseInt(line);
                    pp[i] = x;
                    System.out.println(pp[i]);
                    i+=1;
                }

            }
            in.close();
            proc.waitFor();
            if(pp[0]==-1) {
                Assert.fail("定位值：" + locateValue + "未找到");
                logger.error("未通过" + locateWay + "方式定位到元素：" + locateValue);
            } else {
                return pp;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        return pp;
    }

    public String execScript(String filename, String params) {
        File file = new File(filename);
        String fileName = file.getName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String[] paramLists = params.split(",");
        String command = "";
        for (String p : paramLists) {
            command += " " + p;
        }
        if ("py".equals(suffix)) {
            command = "python3 " + filename + command;

        } else if ("jar".equals(suffix)) {
            command = "java -jar " + fileName + command;
        } else {
            return String.format("%s类型暂时不支持", suffix);
        }
        try {
            logger.info("调用外部执行命令为:" + command);
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line + "\n");
            }
            logger.info("执行外部脚本结果为" + sb.toString());
            return sb.toString();
        } catch (Exception e) {
            logger.info("调用外部脚本异常信息为:" + e.toString());
            return e.toString();
        }
    }

    public void setValueByJs(String way, String value, String locationElement, String text) {
        //通过js修改元素的值
        String jsWay = "";
        switch (way.toUpperCase()) {
            case "ID":
                jsWay = String.format("document.getElementById(\"%s\")", value);
            case "NAME":
                jsWay = String.format("document.getElementsByName(\"%s\")[0]", value);
            case "TAGNAME":
                jsWay = String.format("document.getElementsByTagName(\"%s\")[0]", value);
            case "CLASSNAME":
                jsWay = String.format("document.getElementsByClassName(\"%s\")[0]", value);
        }
        String js = String.format("var ele=%s;ele.removeAttribute(\"readonly\");ele.value=\"%s\";", jsWay, text);
        jsexecut(js);
    }


    public void assertCss(String way, String value, String locationElement, String cssFlag, String except) throws Exception {
        //获取元素的cssValue值
        try {
            WebElement element = findView(way, value, locationElement);
            String result = element.getCssValue(cssFlag);
            assert result.equals(except);
            saveScreenshotPNG();
        } catch (Exception e) {
            logger.error("断言元素CSS 属性值不正确");
            Assert.fail("断言元素CSS属性值不正确");
            throw e;
        }
    }

    private boolean elementIsDisplay(String locateWay, String locateValue, int time) {
        try {
            long t = Long.parseLong(String.valueOf(time));
            long beging_time = new Date().getTime();
            long current_time = 0;
            while (current_time < (beging_time + t)) {
                WebElement element = (WebElement) findView(locateWay, locateValue);
                if (element.isDisplayed() == true) {
                    this.saveScreenshotPost(base.getCurrentPage() + "display" + ++base.stepIndex);
                    return true;
                }
            }
            logger.info("超时未找到");
            return false;
        } catch (Exception e) {
            Assert.fail("元素定位失败");
            return false;
        }
    }

    public boolean isElementDisplay(String locateWay, String locateValue, int time) {
        try {
            String times = String.valueOf(time);
            boolean end = ele_present(locateWay, locateValue, times);

            return end;
        } catch (NoSuchElementException | InterruptedException ex) {

            return false;
        }
    }

    public boolean isElementNotDisplay(String locateWay, String locateValue, int time) {
        try {
            String times = String.valueOf(time);
            boolean end = ele_present(locateWay, locateValue, times);
            if (end == true){
                return false;
            }else {
                return true;
            }
//            return end;
        } catch (NoSuchElementException | InterruptedException ex) {

            return true;
        }
    }
    public boolean ele_present(String locateWay, String locateValue, String time) throws InterruptedException {
        long times = Long.parseLong(time);
        long currentTime = 0L;
        long starttime = 0l;
        starttime = new Date().getTime();
        do {

            try {
                findViewForTime(locateWay, locateValue).isDisplayed();
                return true;

            } catch (Exception e) {

            }
            currentTime = new Date().getTime();
            Thread.sleep(500);
        } while (currentTime < starttime + times);
        return false;
    }

    public void waitElementDisplay(String locateWay, String locateValue, int time) {
        try {
//            String times = String.valueOf(time);
            long currentTime = 0L;
            long starttime = 0l;
            starttime = new Date().getTime();
            do {

                try {
                    findViewForTime(locateWay, locateValue);
                    logger.info("等待元素出现成功");
                    break;

                } catch (Exception e) {

                }
                currentTime = new Date().getTime();
                Thread.sleep(500);
            } while (currentTime < starttime + time);
            if(currentTime > starttime + time){
                Assert.fail("等待元素出现超时");
            }

        } catch (NoSuchElementException | InterruptedException ex) {
            Assert.fail("等待元素出现失败");

        }
    }
    public void waitElementDisplay(String text, int times) {
        try {
            long currentTime = 0L;
            long starttime = 0L;
            starttime = new Date().getTime();
            do {

                String locator = "//*[contains(text(), \"" + text + "\")]";
                try {
                    driver.findElement(By.xpath(locator));
                    break;

                } catch (Exception e) {

                }
                currentTime = new Date().getTime();
                Thread.sleep(500);
            } while (currentTime < starttime + times);
            if(currentTime > starttime + times){
                Assert.fail("等待元素出现超时");
            }
        } catch (Exception ex) {
            Assert.fail("等待元素不存在失败");

        }
    }

    public void waitElementNotDisplay(String locateWay, String locateValue, int time) {
        try {
            long currentTime = 0L;
            long starttime = 0L;
            starttime = new Date().getTime();
            do {

                try {
                    findViewForTime(locateWay, locateValue);


                } catch (Exception e) {
                    break;
                }
                currentTime = new Date().getTime();
                Thread.sleep(500);
            } while (currentTime < starttime + time);
            if(currentTime > starttime + time){
                Assert.fail("等待元素消失超时");
            }
        } catch (NoSuchElementException | InterruptedException ex) {
            Assert.fail("等待元素消失失败");

        }
    }
    public void waitElementNotDisplay(String text, int times) {
        try {
            long currentTime = 0L;
            long starttime = 0L;
            starttime = new Date().getTime();
            do {

                String locator = "//*[contains(text(), \"" + text + "\")]";
                try {
                    driver.findElement(By.xpath(locator));
                    break;

                } catch (Exception e) {

                }
                currentTime = new Date().getTime();
                Thread.sleep(500);
            } while (currentTime < starttime + times);
            if(currentTime > starttime + times){
                Assert.fail("等待元素消失超时");
            }
        } catch (Exception ex) {
            Assert.fail("等待元素消失失败");

        }
    }
    public void waitElementNotExist(String locateWay, String locateValue, String locationElement, int times) {
        try {
            long currentTime = 0L;
            long starttime = 0L;
            starttime = new Date().getTime();
            do {

                try {
                    findViewForTime(locateWay, locateValue);


                } catch (Exception e) {
                    break;
                }
                currentTime = new Date().getTime();
                Thread.sleep(500);
            } while (currentTime < starttime + times);
            if(currentTime > starttime + times){
                Assert.fail("等待元素不存在超时");
            }
        } catch (Exception ex) {


        }
    }

    public void waitElementNotExist(String text, int times) {
        try {
            long currentTime = 0L;
            long starttime = 0L;
            starttime = new Date().getTime();
            do {

                String locator = "//*[contains(text(), \"" + text + "\")]";
                try {
                    driver.findElement(By.xpath(locator));


                } catch (Exception e) {
                    break;
                }
                currentTime = new Date().getTime();
                Thread.sleep(500);
            } while (currentTime < starttime + times);
            if(currentTime > starttime + times){
                Assert.fail("等待元素不存在超时");
            }
        } catch (Exception ex) {
            Assert.fail("等待元素不存在失败");

        }
    }
}
