<%@ page isELIgnored="false" %>
<%@ page import="web_app.common.Constants" %>

<html>
    <head>
        <title>Result</title>
    </head>
    <body>
        <h2>Result:</h2>
        <h3>Value: <%= request.getAttribute(Constants.REQUESTED_VALUE_ATTRIBUTE_NAME) %></h3>
        <h3>Prime numbers: <%= request.getAttribute(Constants.VALUE_RESULT_ATTRIBUTE_NAME) %></h3>
    </body>
</html>
