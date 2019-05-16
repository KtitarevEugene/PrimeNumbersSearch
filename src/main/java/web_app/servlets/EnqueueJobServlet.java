package web_app.servlets;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web_app.common.Constants;
import web_app.common.Utils;
import web_app.models.ValueModel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

public class EnqueueJobServlet extends HttpServlet {

    private final Logger logger = LoggerFactory.getLogger(EnqueueJobServlet.class);

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        forwardToForm(req, resp);
    }

    @Override
    protected void doPost(@NotNull HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String value = req.getParameter(Constants.VALUE_FIELD);
        if (Utils.isInteger(value)) {

            ValueModel valueModel = sendRequestToRestApi(Integer.parseInt(value));
            req.setAttribute(Constants.VALUE_ID_ATTRIBUTE_NAME, valueModel.getId());

        }

        forwardToForm(req, resp);
    }

    private void forwardToForm(@NotNull HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute(Constants.VALUE_ATTRIBUTE_NAME, Constants.VALUE_FIELD);

        getServletContext().getRequestDispatcher("/form.jsp").forward(req, resp);
    }

    private ValueModel sendRequestToRestApi(int value) {
        ValueModel valueModel = null;
        Client client = Client.create();

        WebResource resource = client.resource(String.format("http://localhost:8080/PrimeNumbersRestService/services/numbers/send/%d", value));

        ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

        if (response.getStatus() == ClientResponse.Status.OK.getStatusCode()) {
            valueModel = response.getEntity(ValueModel.class);
        }

        return valueModel;
    }
}
