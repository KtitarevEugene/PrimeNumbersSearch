package web_app.models;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "Value")
public class ValueModel {
    private int id;
    private int value;

    public ValueModel() {}

    public ValueModel(int id, int value) {
        this.id = id;
        this.value = value;
    }
}
