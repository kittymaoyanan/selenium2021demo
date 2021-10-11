package dp;


import com.google.common.collect.Lists;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

//所有的输入组参数

@XStreamAlias("inputParameter")
public class InputParameter {

    @XStreamImplicit
    private List<InputGroup> inputGroups;

    @XStreamImplicit
    private List<InputValueRefer> datas = Lists.newArrayList();

    public List<InputGroup> getInputGroups() {
        return inputGroups;
    }

    public void setInputGroups(List<InputGroup> inputGroups) {
        this.inputGroups = inputGroups;
    }


    public List<InputValueRefer> getDatas() {
        return datas;
    }

    public void setDatas(List<InputValueRefer> datas) {
        this.datas = datas;
    }

    public static void main(String[] args) throws Exception
    {
        InputParameter inputParameter = new InputParameter();

        List<InputGroup> inputGroups = new ArrayList<>();


        InputGroup inputGroup1 = new InputGroup();
        inputGroup1.setName("输入组");
        inputGroup1.setDpMethodName("dp_01");
        List<InputValue> values = Lists.newArrayList();
        InputValue v1 = new InputValue();
        v1.setName("param1");
        v1.setValue("value2");
        values.add(v1);

        InputValue v2 = new InputValue();
        v2.setName("param2");
        v2.setValue("value2");
        values.add(v2);


        List<InputValueLoop> loops = Lists.newArrayList();
        InputValueLoop loop = new InputValueLoop();
        loop.setValues(values);
        loops.add(loop);
        inputGroup1.setLoops(loops);

        inputGroups.add(inputGroup1);
        inputParameter.setInputGroups(inputGroups);


        XStream xstream = new XStream();
        //xstream.addImplicitCollection(Page.class, "pageElements");
        xstream.processAnnotations(new Class[]{InputParameter.class});
        String string2 = xstream.toXML( inputParameter ) ;
        System.out.println(string2);


        String  fileName = "input.xml";
        FileWriter fw = new FileWriter(fileName);
        BufferedWriter writer = new BufferedWriter(fw);
        writer.write(string2);
        writer.flush();
        writer.close();
    }
}
