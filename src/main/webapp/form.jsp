<%@ page isELIgnored="false" %>

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
    </body>
</html>