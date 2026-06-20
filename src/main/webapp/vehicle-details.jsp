<%--
  Created by IntelliJ IDEA.
  User: gemel
  Date: 19/06/2026
  Time: 23:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
    <title>Dettaglio Veicolo</title>

  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/css/style.css">

</head>
<body>

<jsp:include page="/jsp/header.jsp" />

<div class="vehicle-details-card">

  <h1>${vehicle.brand} ${vehicle.model}</h1>

  <p>Anno: ${vehicle.productionYear}</p>
  <p>Km: ${vehicle.km}</p>
  <p>Carburante: ${vehicle.fuelType}</p>
  <p>Cambio: ${vehicle.transmission}</p>
  <p>Prezzo: € ${vehicle.price}</p>
  <p>Stato: ${vehicle.status}</p>
  <p>Descrizione: ${vehicle.description}</p>

  <a  class="details-button"
      href="${pageContext.request.contextPath}/catalog">
    Torna al catalogo
  </a>

</div>

</body>
</html>
