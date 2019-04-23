package web_app.servlets;

import web_app.common.Constants;
import web_app.messaging.JMSConnection;
import web_app.messaging.JMSProducer;
import web_app.messaging.JMSSession;

import javax.jms.JMSException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EnqueueJobServlet extends HttpServlet {

    private JMSConnection connection;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            connection = new JMSConnection(Constants.CLIENT_ID);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        forwardToForm(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String value = req.getParameter(Constants.VALUE_FIELD);
            if (!value.isEmpty()) {
                String jmsId = sendResultMessage(req.getParameter(Constants.VALUE_FIELD));

                req.setAttribute(Constants.JMS_ID_ATTRIBUTE_NAME, jmsId);
            }

            forwardToForm(req, resp);

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private void forwardToForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute(Constants.VALUE_ATTRIBUTE_NAME, Constants.VALUE_FIELD);

        getServletContext().getRequestDispatcher("/form.jsp").forward(req, resp);
    }

    private String sendResultMessage(String resultMessage) throws JMSException {
        try (JMSSession session = new JMSSession(connection)) {
            try (JMSProducer producer = new JMSProducer(session, Constants.QUEUE_NAME)) {
                return producer.send(session.createMessage(resultMessage));
            }
        }
    }
}
