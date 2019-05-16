package web_app.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@XmlRootElement(name = "ValueResult")
public class ValueResultModel extends ValueModel {

    private List<Integer> primeNumbers;

    public ValueResultModel() { super(); }

    public ValueResultModel(int id, int value, List<Integer> primeNumbers) {
        super(id, value);

        this.primeNumbers = primeNumbers;
    }
}
