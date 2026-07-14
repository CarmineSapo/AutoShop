<%--
  Created by IntelliJ IDEA.
  User: gemel
  Date: 02/07/2026
  Time: 20:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="UTF-8">
    <title>Area concessionario</title>

  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/css/style.css">
</head>

<body>

<jsp:include page="/jsp/header.jsp"/>

<div class="dealer-dashboard">

  <h1>Area concessionario</h1>

  <p>Benvenuto,
  <strong>${sessionScope.user.username}</strong>.
  </p>

  <p>
    Da questa sezione potrai gestire i veicoli pubblicati.
  </p>

  <div class="dealer-actions">

    <a class="details-button"
       href="${pageContext.request.contextPath}/dealer/vehicles">
      I miei veicoli
    </a>

    <a class="details-button"
       href="${pageContext.request.contextPath}/dealer/add-vehicle">
      Aggiungi veicolo
    </a>

  </div>

</div>

</body>
</html>
