package dp;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

@XStreamAlias("loop")
public class InputValueLoop {

    @XStreamImplicit
    private List<InputValue> values;

    public List<InputValue> getValues() {
        return values;
    }

    public void setValues(List<InputValue> values) {
        this.values = values;
    }
}
