<%--
  Created by IntelliJ IDEA.
  User: gemel
  Date: 21/06/2026
  Time: 18:14
  To change this template use File | Settings | File Templates.
--%>
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

        <label>Email</label>
        <input type="email" name="email" required>

        <label>Password</label>
        <input type="password"  name="password" required>

        <button type="submit">Accedi</button>
    </form>

</div>
</body>
</html>
