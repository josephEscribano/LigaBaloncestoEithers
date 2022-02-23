<%--
  Created by IntelliJ IDEA.
  User: Joseph
  Date: 26/12/2021
  Time: 16:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>CambioPass</title>
</head>
<body>
    <form action="${pageContext.request.contextPath}/cambio" method="get">

        <input type="password" name = "pass" />
        <input type="hidden" name="codigo" value="${codigo}">
        <input type="submit" value="Cambiar Contraseña">
    </form>
</body>
</html>
