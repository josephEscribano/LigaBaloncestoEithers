<%--
  Created by IntelliJ IDEA.
  User: Joseph
  Date: 24/12/2021
  Time: 16:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Fuera de tiempo</title>
</head>
<body>
    <p>Has excedido el tiempo limite para poder verificar el correo, pulsa el siguiente boton si quieres que reenviemos el email</p>

    <form action="${pageContext.request.contextPath}/mail" method="get">
        <input type="hidden" name="codigo" value="${codigo}">

        <input type="submit" value="Enviar Email">
    </form>

</body>
</html>
