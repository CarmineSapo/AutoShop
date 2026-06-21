<%--
  Created by IntelliJ IDEA.
  User: gemel
  Date: 21/06/2026
  Time: 19:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="UTF-8">
    <title>Profilo</title>

  <link rel="stylesheet"
  href="${pageContext.request.contextPath}/css/style.css">

</head>
<body>

<jsp:include page="/jsp/header.jsp"/>

<div class="form-card">

  <h1>Profilo utente</h1>

  <p><strong>Username:</strong> ${sessionScope.user.username}</p>
  <p><strong>Email:</strong> ${sessionScope.user.email}</p>
  <p><strong>Ruolo:</strong> ${sessionScope.user.role}</p>
</div>

</body>
</html>
