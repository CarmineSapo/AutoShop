<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">

</head>
<body>

<jsp:include page="/jsp/header.jsp" />

<div class="form-card">

    <h1>Login</h1>

    <c:if test="${not empty error}">
        <p class="error-message">${error}</p>
    </c:if>

    <form action="${pageContext.request.contextPath}/login" method="post">

        <label for="loginEmail">Email</label>
        <input type="email"
               id="loginEmail"
               name="email"
               maxlength="100"
               required>

        <label for="loginPassword">Password</label>
        <input type="password"
               id="loginPassword"
               name="password"
               maxlength="72"
               required>

        <button type="submit">Accedi</button>


        <p class="auth-switch">
            Non hai ancora un account?

            <a href="${pageContext.request.contextPath}/register.jsp" title="Area registrazione">
                Registrati
            </a>

        </p>

    </form>

</div>
</body>
</html>
