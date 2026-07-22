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

    <label for="username">Username</label>
    <input type="text"
           id="username"
           name="username"
           minlength="3"
           maxlength="30"
           value="<c:out value='${param.username}'/>"
           required>

    <label for="email">Email</label>
    <input type="email"
           id="email"
           name="email"
           maxlength="100"
           value="<c:out value='${param.email}'/>"
           required>

    <label for="password">Password</label>
    <input type="password"
           id="password"
           name="password"
           minlength="8"
           maxlength="72"
           required>

    <button type="submit">Crea account</button>


    <p class="auth-switch">
      Hai già un account?

      <a href="${pageContext.request.contextPath}/login.jsp">
        Accedi
      </a>

    </p>

  </form>

</div>

</body>
</html>
