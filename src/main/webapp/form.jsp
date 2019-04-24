<%@ page isELIgnored="false" %>
<%@ page import="web_app.common.Constants" %>

<html>
    <head>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
        <script src="js/validate.js"></script>
    </head>
    <body>
        <h2>Enter number:</h2>
        <form action="/PrimeNumbersSearch/form" method="post">
            <input class="numeric" type="text" name="${fieldName}" />
            <input type="submit" value="Send">
        </form>
        <%
            Object attribute = request.getAttribute(Constants.VALUE_ID_ATTRIBUTE_NAME);
            if (attribute instanceof Integer) {
                Integer valueId = (Integer) attribute;
                if (valueId != -1) { %>
                    <a href="/PrimeNumbersSearch/result?<%= Constants.VALUE_ID_PARAM %>=<%= valueId %>">Open Result</a>
            <% } %>
        <% } %>

    </body>
</html>