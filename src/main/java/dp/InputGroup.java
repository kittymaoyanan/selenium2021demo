package dp;

import com.google.common.collect.Lists;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.List;
import java.util.Map;

@XStreamAlias("inputGroup")
public class InputGroup {

    @XStreamAlias("name")
    private String name;  //输入组名称


    @XStreamAlias("dpMethodName")
    private String dpMethodName;  //输入组名称


    @XStreamImplicit
    private List<InputValueLoop> loops;


    @XStreamOmitField
    private List<Map<String, String>> valueMap; //dp使用时的迭代map



    public InputGroup() {
        this.name = "输入组";
        this.valueMap = Lists.newArrayList();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<InputValueLoop> getLoops() {
        return loops;
    }

    public void setLoops(List<InputValueLoop> loops) {
        this.loops = loops;
    }

    public List<Map<String, String>> getValueMap() {
        return valueMap;
    }

    public void setValueMap(List<Map<String, String>> valueMap) {
        this.valueMap = valueMap;
    }

    public String getDpMethodName() {
        return dpMethodName;
    }

    public void setDpMethodName(String dpMethodName) {
        this.dpMethodName = dpMethodName;
    }

}
