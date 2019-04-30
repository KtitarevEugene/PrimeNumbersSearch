package web_app.servlets;

import web_app.common.Constants;
import web_app.common.Utils;
import web_app.repository.db.db_models.ResultModel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JobResultServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String valueId = req.getParameter(Constants.VALUE_ID_PARAM);
        if (valueId != null && Utils.isInteger(valueId)) {
            ResultModel model = getResultModelById(Integer.parseInt(valueId));
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

    private ResultModel getResultModelById(int id) {
        return dataRepository.getResultById(id);
    }

    private void setRequestAttributes(HttpServletRequest request, ResultModel model) {
        request.setAttribute(Constants.REQUESTED_VALUE_ATTRIBUTE_NAME, model.getValue());
        request.setAttribute(Constants.VALUE_RESULT_ATTRIBUTE_NAME, model.getPrimeNumbers());
    }

    private void forwardToResultPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        getServletContext().getRequestDispatcher("/result.jsp").forward(request, response);
    }

    private void forwardToErrorPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        getServletContext().getRequestDispatcher("/error.html").forward(request, response);
    }

}
