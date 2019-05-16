package web_app.servlets;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web_app.common.Constants;
import web_app.common.Utils;
import web_app.models.ValueResultModel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

public class JobResultServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(JobResultServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String valueId = req.getParameter(Constants.VALUE_ID_PARAM);
        if (valueId != null && Utils.isInteger(valueId)) {
            ValueResultModel model = sendRequestToRestApi(Integer.parseInt(valueId));
            if (model != null) {
                setRequestAttributes(req, model);
                forwardToResultPage(req, resp);
            } else {
                forwardToErrorPage(req, resp);
            }
        } else {
            forwardToErrorPage(req, resp);
        }
    }

    private void setRequestAttributes(HttpServletRequest request, ValueResultModel model) {
        request.setAttribute(Constants.REQUESTED_VALUE_ATTRIBUTE_NAME, model.getValue());
        request.setAttribute(Constants.VALUE_RESULT_ATTRIBUTE_NAME, model.getPrimeNumbers());
    }

    private void forwardToResultPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        getServletContext().getRequestDispatcher("/result.jsp").forward(request, response);
    }

    private void forwardToErrorPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        getServletContext().getRequestDispatcher("/error.html").forward(request, response);
    }

    private ValueResultModel sendRequestToRestApi(int valueId) {
        ValueResultModel resultModel = null;

        Client client = Client.create();

        WebResource resource = client.resource(String.format("http://localhost:8080/PrimeNumbersRestService/services/numbers/%d", valueId));

        ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

        if (response.getStatus() == 200) {
            resultModel = response.getEntity(ValueResultModel.class);
        }

        return resultModel;
    }
}
