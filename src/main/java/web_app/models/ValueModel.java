package web_app.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "Value")
public class ValueModel {
    private int id;
    private int value;

    public ValueModel(int id, int value) {
        this.id = id;
        this.value = value;
    }
}
