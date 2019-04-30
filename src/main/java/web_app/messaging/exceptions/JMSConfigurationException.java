package web_app.messaging.exceptions;

public class JMSConfigurationException extends Exception {
    public JMSConfigurationException() {}

    public JMSConfigurationException(String what) {
        super(what);
    }
}
