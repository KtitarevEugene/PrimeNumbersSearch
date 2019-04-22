package servlet.messaging;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Enumeration;

public class Producer {

    private String clientName;
    private Connection connection;
    private Session session;
    private MessageProducer producer;

    public void createConnection (String clientName, String queueName) throws JMSException {
        this.clientName = clientName;

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_BROKER_URL);

        connection = connectionFactory.createConnection();
        connection.setClientID(clientName);

        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Queue queue = session.createQueue(queueName);

        producer = session.createProducer(queue);
    }

    public void closeConnection() throws JMSException {
        connection.close();
    }

    public void sendResult(String result) throws JMSException {
        TextMessage message = session.createTextMessage(result);

        producer.send(message);
    }
}
