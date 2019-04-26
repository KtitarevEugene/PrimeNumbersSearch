package web_app.servlets;

import web_app.common.Constants;
import web_app.common.Utils;
import web_app.repository.DataRepository;
import web_app.repository.cache.cache_managers.MemcachedManager;
import web_app.messaging.JMSConnection;
import web_app.messaging.JMSMessage;
import web_app.messaging.JMSProducer;
import web_app.messaging.JMSSession;
import web_app.repository.db.db_managers.MySQLConnectorManager;
import web_app.repository.db.db_models.ResultModel;

import javax.jms.JMSException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class EnqueueJobServlet extends HttpServlet {

    private JMSConnection connection;
    private DataRepository dataRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            connection = new JMSConnection(Constants.CLIENT_ID);
        } catch (JMSException e) {
            e.printStackTrace();
        }

        dataRepository = new DataRepository(
                new MySQLConnectorManager(Constants.DB_USER,Constants.DB_PASSWORD),
                new MemcachedManager("localhost", 11211));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        forwardToForm(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
            e.printStackTrace();
        }
    }

    private void forwardToForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute(Constants.VALUE_ATTRIBUTE_NAME, Constants.VALUE_FIELD);

        getServletContext().getRequestDispatcher("/form.jsp").forward(req, resp);
    }

    private String sendResultMessage(int valueId) throws JMSException {
        try (JMSSession session = new JMSSession(connection)) {
            try (JMSProducer producer = new JMSProducer(session, Constants.QUEUE_NAME)) {
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
            e.printStackTrace();
        }

        return valueId;
    }
}
