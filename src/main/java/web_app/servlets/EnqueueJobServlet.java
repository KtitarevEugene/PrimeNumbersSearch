package web_app.servlets;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web_app.common.Constants;
import web_app.common.Utils;
import web_app.messaging.JMSConnection;
import web_app.messaging.JMSMessage;
import web_app.messaging.JMSProducer;
import web_app.messaging.JMSSession;
import web_app.messaging.exceptions.JMSConfigurationException;
import web_app.repository.db.db_models.ResultModel;

import javax.jms.JMSException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class EnqueueJobServlet extends BaseServlet {

    private final Logger logger = LoggerFactory.getLogger(EnqueueJobServlet.class);

    private JMSConnection connection;

    @Override
    public void init() throws ServletException {
        super.init();

        try {
            Properties properties = (Properties) getServletContext()
                    .getAttribute(Constants.CONFIG_PROPERTIES_ATTRIBUTE_NAME);

            connection = new JMSConnection(properties);
        } catch (JMSException e) {
            logger.error("JMSException has been thrown.", e);
        } catch (JMSConfigurationException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        forwardToForm(req, resp);
    }

    @Override
    protected void doPost(@NotNull HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String value = req.getParameter(Constants.VALUE_FIELD);
            if (value != null && Utils.isInteger(value)) {

                List<ResultModel> resultModels = getResultByValue(value);

                if (resultModels != null && !resultModels.isEmpty()) {
                    req.setAttribute(Constants.VALUE_ID_ATTRIBUTE_NAME, resultModels.get(0).getId());

                } else {
                    int valueId = insertValueToSearch(value);

                    sendResultMessage(valueId);

                    req.setAttribute(Constants.VALUE_ID_ATTRIBUTE_NAME, valueId);
                }
            }

            forwardToForm(req, resp);

        } catch (JMSException e) {
            logger.error("JMSException has been thrown.", e);
        }
    }

    private void forwardToForm(@NotNull HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute(Constants.VALUE_ATTRIBUTE_NAME, Constants.VALUE_FIELD);

        getServletContext().getRequestDispatcher("/form.jsp").forward(req, resp);
    }

    private String sendResultMessage(int valueId) throws JMSException {
        try (JMSSession session = new JMSSession(connection)) {
            Properties properties = (Properties) getServletContext()
                    .getAttribute(Constants.CONFIG_PROPERTIES_ATTRIBUTE_NAME);
            try (JMSProducer producer = new JMSProducer(session,
                    properties.getProperty(Constants.ACTIVE_MQ_QUEUE_NAME))) {
                JMSMessage message = session.createMessage(String.valueOf(valueId));

                return producer.send(message);
            }
        }
    }

    private List<ResultModel> getResultByValue (String value) {
        return dataRepository.getResultByValue(value);
    }

    private int insertValueToSearch(String value) {
        int valueId = -1;

        try {
            return dataRepository.insertRequestedValue(value);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return valueId;
    }
}
