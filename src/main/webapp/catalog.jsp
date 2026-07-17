<%@ page contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8"
         language="java" %>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="it">

<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>AutoShop</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">
</head>

<body>

<jsp:include page="/jsp/header.jsp"/>

<main class="catalog-container">

    <c:choose>

        <c:when test="${param.showFilters eq 'true'}">

            <h1>Cerca un veicolo</h1>

            <div class="catalog-filters">
                <p>
                    Qui inseriremo i filtri.
                </p>
            </div>

        </c:when>

        <c:otherwise>

            <h1>Veicoli disponibili</h1>

        </c:otherwise>

    </c:choose>

    <c:choose>

        <c:when test="${empty vehicles}">

            <p>Nessun veicolo disponibile.</p>

        </c:when>

        <c:otherwise>

            <div class="vehicle-container">

                <c:forEach var="vehicle"
                           items="${vehicles}">

                    <div class="vehicle-card">

                        <h2>
                                ${vehicle.brand} ${vehicle.model}
                        </h2>

                        <p>Anno: ${vehicle.productionYear}</p>
                        <p>Km: ${vehicle.km}</p>
                        <p>Carburante: ${vehicle.fuelType}</p>
                        <p>Cambio: ${vehicle.transmission}</p>
                        <p>Prezzo: € ${vehicle.price}</p>
                        <p>Descrizione: ${vehicle.description}</p>

                        <a class="details-button"
                           href="${pageContext.request.contextPath}/vehicle?id=${vehicle.id}">
                            Vedi dettagli
                        </a>

                    </div>

                </c:forEach>

            </div>

        </c:otherwise>

    </c:choose>

</main>

</body>
</html>