package dp;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("inputValue")
public class InputValue {

    @XStreamAlias("name")
    private String name;

    @XStreamAlias("refer")
    private String refer;

    @XStreamAlias("value")
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getRefer() {
        return refer;
    }

    public void setRefer(String refer) {
        this.refer = refer;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
