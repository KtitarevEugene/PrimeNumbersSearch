package web_app.messaging;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSConnection implements AutoCloseable {

    private String clientName;
    private Connection connection;

    public JMSConnection(String clientName) throws JMSException {
        this.clientName = clientName;

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_BROKER_URL);

        connection = connectionFactory.createConnection();
        connection.setClientID(clientName);
    }

    @Override
    public void close() throws JMSException {
        connection.close();
    }

    Session createSession() throws JMSException {
        return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    public String getClientName() {
        return clientName;
    }
}
