/*
 *
 * 此文件由龙测科技(1.0)自动产生。
 *
 */

package testcase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;
import org.openqa.selenium.WebDriver;
import java.util.concurrent.TimeUnit;
import core.BaseTest;
import utilities.Log;
import Listener.TestListener;
import fields.Elements;
import io.qameta.allure.*;
import org.testng.collections.Lists;
import java.util.Map;

@Listeners({TestListener.class})
public class Testguozilogin extends BaseTest {

    @BeforeMethod
    @Override
    @Parameters({"URL"})
    public void setUp(@Optional String URL) throws Exception {
        super.setUp(URL);
        super.addStep("testCase2", Lists.newArrayList("77180", "77192"),
                Lists.newArrayList("登录页面", "企业管理-考核评分"));

        getDriver().get("http://172.16.0.174:7043/");
    }

    @AfterMethod(alwaysRun = true)
    public void end() throws Exception {
        beforeEnd();
        getDriver().quit();
    }

    @Story("新建考核评分南京公司测试")
    @Feature("新建考核评分南京公司")
    @Description("新建考核评分南京公司")
    @Severity(SeverityLevel.BLOCKER)
    @Test(dataProvider = "dp_15167_1", dataProviderClass = DataProvider_Testguozilogin.class, priority = 2)
    public void testCase2(Map<String, String> params) throws Exception {
        // 1. 首页面 --- sleep ---> ----->登录页面

        // click -->登录按钮
        dt.click(Elements.loginbtn_by, Elements.loginbtn,
                Elements.loginbtn_name);
        // sleep: 1
        dt.sleep(1);
        // frame: layui-layer-iframe1
        dt.frame("layui-layer-iframe1");
        if (params.get("inputid_268863552_0") != null) {
            // inputText--->用户名
            dt.inputText(Elements.username_by, Elements.username,
                    Elements.username_name, params.get("inputid_268863552_0"));
        }
        // sleep: 1
        dt.sleep(1);
        if (params.get("inputid_268863552_1") != null) {
            // inputText--->密码
            dt.inputText(Elements.psd_by, Elements.psd, Elements.psd_name,
                    params.get("inputid_268863552_1"));
        }
        // click -->登录2
        dt.click(Elements.logintwo_by, Elements.logintwo,
                Elements.logintwo_name);
        // sleep: 2
        dt.sleep(2);
        super.refreshStep();
        // 2. 登录页面 --- sleep ---> ----->企业管理-考核评分
        // sleep: 2
        dt.sleep(2);
        // click -->公司管理
        dt.click(Elements.divtaga9czu_by, Elements.divtaga9czu,
                Elements.divtaga9czu_name);
        // click -->考核评分左边
        dt.click(Elements.divtagt4l6k_by, Elements.divtagt4l6k,
                Elements.divtagt4l6k_name);
        // frame: frame
        dt.frame("frame");
        // click -->考核评分
        dt.click(Elements.spantagkrojl_by, Elements.spantagkrojl,
                Elements.spantagkrojl_name);
        // click -->考核评分添加
        dt.click(Elements.atagkwt4s_by, Elements.atagkwt4s,
                Elements.atagkwt4s_name);
        // frame: layui-layer-iframe1
        dt.frame("layui-layer-iframe1");
        // click -->年度考核单选按钮iTagJKP5W
        dt.click(Elements.itagh4yxj_by, Elements.itagh4yxj,
                Elements.itagh4yxj_name);
        // click -->考核评分添加请选择年份
        dt.click(Elements.yearcheck_by, Elements.yearcheck,
                Elements.yearcheck_name);
        // click -->选择2021年份
        dt.click(Elements.litag593fo_by, Elements.litag593fo,
                Elements.litag593fo_name);
        // click -->考核评分选择时间确定
        dt.click(Elements.spantagf6twk_by, Elements.spantagf6twk,
                Elements.spantagf6twk_name);
        // sleep: 2
        dt.sleep(2);
        // click -->奥灶馆有限公司南
        dt.click(Elements.spantag3u9df_by, Elements.spantag3u9df,
                Elements.spantag3u9df_name);
        // click -->考核评分新增页面的提交
        dt.click(Elements.saved_by, Elements.saved, Elements.saved_name);
        // sleep: 2
        dt.sleep(2);
        super.refreshStep();
    }
}
