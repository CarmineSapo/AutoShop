<%--
  Created by IntelliJ IDEA.
  User: gemel
  Date: 22/06/2026
  Time: 17:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">
</head>

<body>

<jsp:include page="/jsp/header.jsp"/>

<div class="form-card">

    <h1>Ordine confermato</h1>

    <p>Il tuo ordine è stato registrato correttamente.</p>

    <p>
        Numero ordine:
        <strong>${orderId}</strong>
    </p>

    <a class="details-button"
       href="${pageContext.request.contextPath}/catalog">
        Torna al catalogo
    </a>
</div>
</body>
</html>
