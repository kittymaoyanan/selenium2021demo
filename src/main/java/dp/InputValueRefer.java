package dp;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.Objects;

@XStreamAlias("value")
public class InputValueRefer implements Comparable<InputValueRefer> {


    @XStreamAlias("value")
    private String value;


    @XStreamAlias("desc")
    private String desc;

    @XStreamAlias("sequence")
    private int sequence;


    @XStreamAlias("key")
    private String key;

    public InputValueRefer(String value, String desc, int sequence, String key) {
        this.value = value;
        this.desc = desc;
        this.sequence = sequence;
        this.key = key;
    }

    public InputValueRefer() {
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InputValueRefer)) return false;
        InputValueRefer that = (InputValueRefer) o;
        return getSequence() == that.getSequence() &&
                Objects.equals(getDesc(), that.getDesc());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDesc(), getSequence());
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int compareTo(InputValueRefer o) {
        if(this.getDesc().equals(o.getDesc()))
        {
            return this.getSequence().compareTo(o.getSequence());
        }
        else
        {
            return this.getDesc().compareTo(o.getDesc());
        }
    }
}
