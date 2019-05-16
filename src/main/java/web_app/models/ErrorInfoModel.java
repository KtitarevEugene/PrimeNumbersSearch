package web_app.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "employee")
public class ErrorInfoModel {
    private int status;
    private String message;

    public ErrorInfoModel(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
