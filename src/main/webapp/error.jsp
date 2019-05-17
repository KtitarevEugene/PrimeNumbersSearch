<%@ page isELIgnored="false" %>
<%@ page import="web_app.common.Constants" %>

<html>
<head>
    <title>404</title>
</head>
<body>
 <% Object attr = request.getAttribute(Constants.ERROR_TEXT_ATTRIBUTE_NAME);
    if (attr instanceof String) {
        String error = (String) attr;
        if (!error.isEmpty()) { %>
            <h2><%= error %></h2>
     <% } else { %>
            <h2>Unknown error</h2>
     <% } %>
 <% } %>
</body>
</html>