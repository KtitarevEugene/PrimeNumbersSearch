package web_app.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "ValueResult")
public class ValueResultModel extends ValueModel {

    private List<Integer> primeNumbers;

    public ValueResultModel(int id, int value, List<Integer> primeNumbers) {
        super(id, value);

        this.primeNumbers = primeNumbers;
    }
}
