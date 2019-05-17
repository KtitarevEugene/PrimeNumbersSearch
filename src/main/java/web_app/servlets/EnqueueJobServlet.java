package web_app.servlets;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web_app.common.Constants;
import web_app.common.Utils;
import web_app.models.SendResponseModel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

public class EnqueueJobServlet extends HttpServlet {

    private final Logger logger = LoggerFactory.getLogger(EnqueueJobServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        forwardToForm(req, resp);
    }

    @Override
    protected void doPost(@NotNull HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int value = Integer.parseInt(req.getParameter(Constants.VALUE_FIELD));

            SendResponseModel responseModel = sendRequestToRestApi(value);

            if (responseModel.getData() != null) {
                forwardToForm(req, resp, responseModel);
            } else {
                forwardToErrorPage(req, resp, responseModel);
            }

        } catch (NumberFormatException ex) {

            forwardToForm(req, resp);
        }
    }

    private void forwardToForm(@NotNull HttpServletRequest req, @NotNull HttpServletResponse resp, @NotNull SendResponseModel model) throws ServletException, IOException {
        req.setAttribute(Constants.VALUE_ID_ATTRIBUTE_NAME, model.getData().getId());

        forwardToForm(req, resp);
    }

    private void forwardToForm(@NotNull HttpServletRequest req, @NotNull HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute(Constants.VALUE_ATTRIBUTE_NAME, Constants.VALUE_FIELD);

        getServletContext().getRequestDispatcher("/form.jsp").forward(req, resp);
    }

    private void forwardToErrorPage(HttpServletRequest request,
                                    HttpServletResponse response,
                                    SendResponseModel model) throws ServletException, IOException {
        request.setAttribute(Constants.ERROR_TEXT_ATTRIBUTE_NAME, model.getReasonPrase());

        getServletContext().getRequestDispatcher("/error.jsp").forward(request, response);
    }

    private SendResponseModel sendRequestToRestApi(int value) {
        Client client = Client.create();

        WebResource resource = client.resource(String.format("http://localhost:8080/PrimeNumbersRestService/services/numbers/send/%d", value));

        ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).put(ClientResponse.class);

        return response.getEntity(SendResponseModel.class);
    }
}
