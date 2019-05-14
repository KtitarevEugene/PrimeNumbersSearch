package web_app.servlets;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import web_app.common.Constants;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class JobResultServletTest extends Mockito {

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    ServletContext context;

    @Mock
    ServletConfig config;

    @Mock
    RequestDispatcher dispatcher;

    private List<Integer> primeNumbers;
    private String requestedValue;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        setMainRules();
    }

    private void setMainRules() {
        when(context.getInitParameter(Constants.ENV_VAR_NAME)).thenReturn("WebCfgPath");
        when(context.getRequestDispatcher(anyString())).thenReturn(dispatcher);

        doAnswer(this::answerForRequestSetAttribute).when(request).setAttribute(anyString(), any());
    }

    @Nullable
    private Object answerForRequestSetAttribute (@NotNull InvocationOnMock invocationOnMock) {
        Object arg0 = invocationOnMock.getArguments()[0];
        if (arg0 instanceof String) {
            String key = (String) arg0;

            switch (key) {
                case Constants.REQUESTED_VALUE_ATTRIBUTE_NAME:
                    requestedValue = (String) invocationOnMock.getArguments()[1];
                    break;
                case Constants.VALUE_RESULT_ATTRIBUTE_NAME:
                    primeNumbers = (List<Integer>) invocationOnMock.getArguments()[1];
                    break;
            }
        }

        return null;
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        JobResultServlet servlet = initServlet();
        servlet.doGet(request, response);
    }

    public void setRuleForGettingValueId(int valueId) {
        when(request.getParameter(Constants.VALUE_ID_PARAM)).thenReturn(String.valueOf(valueId));
    }

    @NotNull
    private JobResultServlet initServlet() throws ServletException {
        JobResultServlet servlet = new JobResultServlet() {

            @Contract(pure = true)
            @Override
            public ServletContext getServletContext() {
                return context;
            }
        };
        servlet.init(config);

        return servlet;
    }

    public List<Integer> getPrimeNumbers() {
        return primeNumbers;
    }

    public String getRequestedValue() {
        return requestedValue;
    }
}
