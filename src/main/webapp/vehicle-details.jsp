<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"  uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, initial-scale=1.0">
    <title>Dettaglio Veicolo</title>

  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/css/style.css">

</head>
<body>

<jsp:include page="/jsp/header.jsp" />

<div class="vehicle-details-card">

  <c:choose>

    <c:when test="${not empty vehicle.imagePaths}">

      <div class="vehicle-gallery">

        <c:forEach var="imagePath"
                   items="${vehicle.imagePaths}">

          <img src="${pageContext.request.contextPath}/${imagePath}"
               alt="${vehicle.brand} ${vehicle.model}">

        </c:forEach>

      </div>

    </c:when>

    <c:otherwise>

      <div class="vehicle-image-placeholder">
        Nessuna immagine disponibile
      </div>

    </c:otherwise>

  </c:choose>

  <h1>${vehicle.brand} ${vehicle.model}</h1>

  <p>Anno: ${vehicle.productionYear}</p>
  <p>Km: ${vehicle.km}</p>
  <p>Carburante: ${vehicle.fuelType}</p>
  <p>Cambio: ${vehicle.transmission}</p>
  <p>Prezzo: € ${vehicle.price}</p>
  <p>Stato: ${vehicle.status}</p>
  <p class="vehicle-description">Descrizione: ${vehicle.description}</p>

  <c:url var="dealerProfileUrl"
         value="/dealer-profile">

    <c:param name="id"
             value="${vehicle.dealerId}"/>

  </c:url>

  <p>
    <strong>Venditore:</strong>

    <a href="${dealerProfileUrl}">
      Visualizza profilo dealer
    </a>
  </p>

  <a  class="details-button"
      href="${pageContext.request.contextPath}/catalog">
    Torna al catalogo
  </a>

  <c:choose>

    <%--
        se l'utente autenticato è il proprietario
        del veicolo, non può acquistarlo
    --%>
    <c:when test="${not empty sessionScope.user
                  and sessionScope.user.id eq vehicle.dealerId}">

      <p class="owner-vehicle-message">
        Questo veicolo appartiene alla tua concessionaria.
      </p>

    </c:when>

    <c:otherwise>

      <a class="details-button"
         href="${pageContext.request.contextPath}/add-to-cart?id=${vehicle.id}">
        Aggiungi al carrello
      </a>

    </c:otherwise>

  </c:choose>

</div>

</body>
</html>
