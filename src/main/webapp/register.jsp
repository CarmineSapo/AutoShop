<%--
  Created by IntelliJ IDEA.
  User: gemel
  Date: 20/06/2026
  Time: 21:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
    <title>Registrazione</title>

  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/css/style.css">

</head>

<body>

<jsp:include page="/jsp/header.jsp" />

<div class="form-card">

  <h1>Registrati</h1>

  <c:if test="${not empty error}">
    <p class="error-message">${error}</p>
  </c:if>

  <form action="${pageContext.request.contextPath}/register" method="post">

    <label>Username</label>
    <input type="text" name="username" required>

    <label>Email</label>
    <input type="email" name="email" required>

    <label>Password</label>
    <input type="password" name="password" required>

    <button type="submit">Crea account</button>

  </form>

</div>

</body>
</html>
