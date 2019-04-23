<%@ page isELIgnored="false" %>
<%@ page import="web_app.common.Constants" %>

<html>
    <head>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
        <script src="js/validate.js"></script>
    </head>
    <body>
        <h2>Enter number:</h2>
        <form action="/FirstServlet/form" method="post">
            <input class="numeric" type="text" name="${fieldName}" />
            <input type="submit" value="Send">
        </form>
        <%
            Object attribute = request.getAttribute(Constants.JMS_ID_ATTRIBUTE_NAME);
            if (attribute instanceof String) {
                String jmsId = (String) attribute;
                if (!jmsId.isEmpty()) { %>
                    <a href="/FirstServlet/result?<%= Constants.JMS_ID_PARAM %>=<%= jmsId %>">Open Result</a>
            <% } %>
        <% } %>

    </body>
</html>