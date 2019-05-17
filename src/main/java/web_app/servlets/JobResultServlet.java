package web_app.servlets;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web_app.common.Constants;
import web_app.models.ResultResponseModel;
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
        try {
            int valueId = Integer.parseInt(req.getParameter(Constants.VALUE_ID_PARAM));

            ResultResponseModel responseModel = sendRequestToRestApi(valueId);
            ValueResultModel resultModel = responseModel.getData();

            if (resultModel != null) {
                forwardToResultPage(req, resp, resultModel);
            } else {
                req.setAttribute(Constants.ERROR_TEXT_ATTRIBUTE_NAME, responseModel.getReasonPrase());
                forwardToErrorPage(req, resp);
            }
        } catch (NumberFormatException ex) {
            req.setAttribute(Constants.ERROR_TEXT_ATTRIBUTE_NAME, "Bad Request");
            forwardToErrorPage(req, resp);
        }
    }

    private void forwardToResultPage(HttpServletRequest request,
                                     HttpServletResponse response,
                                     ValueResultModel model) throws ServletException, IOException {
        request.setAttribute(Constants.REQUESTED_VALUE_ATTRIBUTE_NAME, model.getValue());
        request.setAttribute(Constants.VALUE_RESULT_ATTRIBUTE_NAME, model.getPrimeNumbers());

        getServletContext().getRequestDispatcher("/result.jsp").forward(request, response);
    }

    private void forwardToErrorPage(HttpServletRequest request,
                                    HttpServletResponse response) throws ServletException, IOException {

        getServletContext().getRequestDispatcher("/error.jsp").forward(request, response);
    }

    private ResultResponseModel sendRequestToRestApi(int valueId) {
        Client client = Client.create();

        WebResource resource = client.resource(String.format("http://localhost:8080/PrimeNumbersRestService/services/numbers/%d", valueId));

        ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

        return response.getEntity(ResultResponseModel.class);
    }
}
