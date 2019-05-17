package web_app.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "SendResponse")
public class SendResponseModel extends BaseResponseModel {

    private ValueModel data;

    public SendResponseModel(int status, String reasonPhrase) {
        super(status, reasonPhrase);
    }

    public SendResponseModel(int status, String reasonPhrase, ValueModel data) {
        super(status, reasonPhrase);
        this.data = data;
    }
}
