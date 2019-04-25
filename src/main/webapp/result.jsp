<%@ page isELIgnored="false" %>
<%@ page import="web_app.common.Constants" %>
<%@ page import="java.util.List" %>

<html>
    <head>
        <title>Result</title>
    </head>
    <body>
        <%
            Object attribute = request.getAttribute(Constants.VALUE_RESULT_ATTRIBUTE_NAME);
            if (attribute instanceof List) { %>
                <h2>Result:</h2>
                <h3>Value: <%= request.getAttribute(Constants.REQUESTED_VALUE_ATTRIBUTE_NAME) %></h3>

             <% List<Integer> primeNumbers = (List<Integer>) attribute;
                if (!primeNumbers.isEmpty()) { %>
                <table border="1">
                    <tr>
                        <% for (Integer primeNumber : primeNumbers) { %>
                            <td style="padding:5px"><%= primeNumber %></td>
                        <% } %>
                    </tr>
                </table>
             <% } else { %>
                    <h3>Prime numbers not found</h3>
             <% }
            } else { %>
                <h2>Result:</h2>
                <h3>Value: <%= request.getAttribute(Constants.REQUESTED_VALUE_ATTRIBUTE_NAME) %></h3>
                <h3>Request is being processed. Try again later.</h3>
         <% } %>
    </body>
</html>
