/* 
 *
 * 此文件由龙测科技(1.0)自动产生。
 *
 */

package testcase;
import org.testng.annotations.*;
import java.lang.reflect.Method;
import dp.BasicDataprovider;
import java.util.Iterator;

public class DataProvider_Testguozilogin extends BasicDataprovider {
    public static void dpcall(String m) throws Exception {
        Class<?> c = Class.forName("testcase.DataProvider_Testguozilogin");
        Object obj = c.newInstance();
        Method method = c.getMethod(m);
        method.invoke(obj);
    }

    // 输入组0. { id=268863552 groupName=输入组 associatedAssertionId=273061904}
    // 输入元素1.77180+用户名={91320583718677707R }
    // 输入元素2.77180+密码={qqqq1111 }
    // 断言组{ id=273061904 groupName=断言组}

    @DataProvider(name = "dp_15167_1")
    public Iterator<Object[]> dp_15167_1() {
        return getMatchData();
    }
}
