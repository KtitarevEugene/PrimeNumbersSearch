package web_app.servlets;

import org.jetbrains.annotations.Contract;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web_app.common.Constants;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class EnqueueJobServletTest extends Mockito {

    private final Logger logger = LoggerFactory.getLogger(EnqueueJobServlet.class);

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    ServletConfig config;

    @Mock
    ServletContext context;

    @Mock
    RequestDispatcher dispatcher;

    private HashMap <String, Object> hashMap = new HashMap<>();
    private Integer valueId;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        setMainRules();
        EnqueueJobServlet servlet = initServlet();
        for (int i = 0; i < 10; ++i) {
            int value = generateValue(99999);
            setFormParam(value);
            servlet.doPost(request, response);

            logger.info("-------------------------------------------------------------");
            logger.info("value = {} inserted with id = {}", value, valueId);
            logger.info("-------------------------------------------------------------");
        }
    }

    private int generateValue(int maxVal) {
        return new Random().nextInt(maxVal);
    }

    private void setFormParam(int value) {
        when(request.getParameter(Constants.VALUE_FIELD)).thenReturn(String.valueOf(value));
    }

    private void setMainRules() {
        doAnswer(invocationOnMock -> {
            Object arg0 = invocationOnMock.getArguments()[0];
            if (arg0 instanceof String) {
                String key = (String) arg0;
                if (key.equalsIgnoreCase(Constants.VALUE_ID_ATTRIBUTE_NAME)) {
                    valueId = (Integer) invocationOnMock.getArguments()[1];
                }
            }
            return null;
        }).when(request).setAttribute(anyString(), any());

        when(context.getInitParameter(Constants.ENV_VAR_NAME)).thenReturn("WebCfgPath");
        when(config.getInitParameter(Constants.ENV_VAR_NAME)).thenReturn("WebCfgPath");
        when(context.getRequestDispatcher(any())).thenReturn(dispatcher);

        doAnswer(invocationOnMock -> {
            hashMap.put((String)invocationOnMock.getArguments()[0], invocationOnMock.getArguments()[1]);
            return null;
        }).when(context).setAttribute(anyString(), any());

        when(context.getAttribute(Constants.CONFIG_PROPERTIES_ATTRIBUTE_NAME)).thenAnswer(invocationOnMock ->
                hashMap.get(Constants.CONFIG_PROPERTIES_ATTRIBUTE_NAME));

    }


    private EnqueueJobServlet initServlet() throws ServletException {
        EnqueueJobServlet enqueueJobServlet = new EnqueueJobServlet() {
            @Contract(pure = true)
            @Override
            public ServletContext getServletContext() {
                return context;
            }
        };
        enqueueJobServlet.init(config);

        return enqueueJobServlet;
    }
}
