package dp;

import com.google.common.collect.Maps;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.StreamException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.testng.collections.Lists;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class BasicDataprovider {

    public static Logger logger = Logger.getLogger(BasicDataprovider.class);

    private static String[] telFirst="134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153,178,187,188".split(",");


    private  Map<String, InputGroup>   groupMap;

    public Map<String, InputGroup> getGroupMap() {
        return groupMap;
    }

    public String getExecutingMethodName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stackTrace[3];
        return e.getMethodName();
    }

    public BasicDataprovider(String fileName)
    {
        groupMap = loadInput(fileName);
    }

    public BasicDataprovider()
    {
        groupMap = loadInput("input.xml");
    }


    public Iterator<Object[]> getMatchData()
    {
        String methodName = this.getExecutingMethodName();
        InputGroup ig = this.getGroupMap().get(methodName);
        if(ig == null)
        {
            throw new RuntimeException("驱动方法名{" + methodName + "}没有关联输入组");
        }
        List<Object[]> elements = Lists.newArrayList();
        for(Map<String, String> map: ig.getValueMap()) {
            elements.add(new Object[]{map});
        }
        return elements.iterator();
    }


    public static String generatePhone() {
        int index = randomNum(0, telFirst.length - 1);
        String first = telFirst[index];
        String second = String.valueOf(randomNum(1, 888) + 10000).substring(1);
        String thrid = String.valueOf(randomNum(1, 9100) + 10000).substring(1);
        return first + second + thrid;
    }
    public static int randomNum(int min,int max) {
        if(min > max)
        {
            int temp = max;
            max = min;
            min = temp;
        }

        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
    }



    public  Map<String, InputGroup> loadInput(String fileName) {


        XStream xstream = new XStream();
        XStream.setupDefaultSecurity(xstream);
        xstream.allowTypes(new Class[]{InputParameter.class, InputGroup.class, InputValue.class, InputValueLoop.class, InputValueRefer.class});

        xstream.setClassLoader(InputParameter.class.getClassLoader());
        xstream.alias("inputParameter", InputParameter.class);
        xstream.alias("inputGroup", InputGroup.class);
        xstream.alias("inputValue", InputValue.class);
        xstream.alias("loop", InputValueLoop.class);
        xstream.alias("value", InputValueRefer.class);

        xstream.autodetectAnnotations(true);
        Map<String, InputGroup> groupMap = Maps.newHashMap();

        try {
            ClassLoader classLoader = com.dt.web.DtTestCase.class.getClassLoader();
//            String fileName = "input.xml";
            InputStream in = classLoader.getResourceAsStream(fileName);

            InputParameter inputParameter = (InputParameter) xstream.fromXML(in);
            if(inputParameter == null || CollectionUtils.isEmpty(inputParameter.getInputGroups()))
            {
                return groupMap;
            }
            List<InputGroup> matchGroups = inputParameter.getInputGroups().stream().filter(g-> StringUtils.isNotEmpty(g.getDpMethodName())).collect(Collectors.toList());
            groupMap = matchGroups.stream().collect(Collectors.toMap(InputGroup::getDpMethodName ,Function.identity()));


            Map<String,InputValueRefer> referMap = inputParameter.getDatas().stream().collect(Collectors.toMap(InputValueRefer::getKey, Function.identity()));
            for(InputValueRefer refer:referMap.values())
            {
                if(StringUtils.isEmpty( refer.getValue()))
                {
                    continue;
                }
                if("{#mobile}".equals(refer.getValue()))
                {
                    refer.setValue(generatePhone());
                }else if (Pattern.matches("(^\\{#range_[0-9]{1,9}_[0-9]{1,9}}$)", refer.getValue()))
                {
                    String[] tmp = refer.getValue().split("_");
                    refer.setValue(randomNum(Integer.parseInt(tmp[1]), Integer.parseInt(tmp[2].substring(0, tmp[2].length()-1)))+"");
                }
            }

            for(InputGroup ig:groupMap.values())
            {
                ig.setValueMap(Lists.newArrayList());
                if(CollectionUtils.isNotEmpty(ig.getLoops()))
                {
                    for (InputValueLoop loop : ig.getLoops())
                    {
                        for(InputValue value:loop.getValues())
                        {
                            if(referMap.containsKey(value.getRefer()))
                            {
                                value.setValue(referMap.get(value.getRefer()).getValue());
                            }
                        }
                        ig.getValueMap().add(loop.getValues().stream().collect(Collectors.toMap(InputValue::getName, InputValue::getValue)));
                    }
                }
            }
            logger.info("read input.xml finished");
        }
        catch (StreamException e)
        {
            logger.error("input.xml not exist");
            e.printStackTrace();
            throw new RuntimeException("input.xml不存在");
        }
        catch (NumberFormatException e)
        {
            logger.error("range int 错误");
            e.printStackTrace();
            throw new RuntimeException("输入参数range int 错误");
        }
        return groupMap;
    }
}