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

        <%-- Pagina che mostra solamente il form dei filtri. --%>
        <c:when test="${param.showFilters eq 'true'}">

            <h1>Filtra i veicoli</h1>

            <form action="${pageContext.request.contextPath}/catalog"
                  method="get"
                  class="catalog-filters">

                <div class="form-group">

                    <label for="brand">Marca</label>

                    <input type="text"
                           id="brand"
                           name="brand"
                           maxlength="50">

                </div>

                <div class="form-group">

                    <label for="maxPrice">
                        Prezzo massimo
                    </label>

                    <input type="number"
                           id="maxPrice"
                           name="maxPrice"
                           min="1"
                           step="0.01">

                </div>

                <div class="form-group">

                    <label for="fuelType">
                        Carburante
                    </label>

                    <select id="fuelType"
                            name="fuelType">

                        <option value="">Tutti</option>
                        <option value="Benzina">Benzina</option>
                        <option value="Diesel">Diesel</option>
                        <option value="Elettrico">Elettrico</option>
                        <option value="Ibrido">Ibrido</option>
                        <option value="GPL">GPL</option>

                    </select>

                </div>

                <div class="form-group">

                    <label for="transmission">
                        Cambio
                    </label>

                    <select id="transmission"
                            name="transmission">

                        <option value="">Tutti</option>
                        <option value="Manuale">Manuale</option>
                        <option value="Automatico">Automatico</option>

                    </select>

                </div>

                <div class="filter-buttons">

                    <button type="submit"
                            class="details-button">
                        Applica filtri
                    </button>

                </div>

            </form>

        </c:when>

        <%-- Home: mostra tutti i veicoli oppure i risultati filtrati. --%>
        <c:otherwise>

            <h1>
                    ${filtersApplied
                            ? 'Veicoli filtrati'
                            : 'Veicoli disponibili'}
            </h1>

            <c:if test="${not empty filterError}">

                <div class="form-errors">
                    <p>${filterError}</p>
                </div>

            </c:if>

            <%--
                Il pulsante compare soltanto quando è stato
                applicato almeno un filtro.
            --%>
            <c:if test="${filtersApplied}">

                <div class="filters-reset-bar">

                    <a class="details-button"
                       href="${pageContext.request.contextPath}/catalog">
                        Rimuovi filtri
                    </a>

                </div>

            </c:if>

            <c:choose>

                <c:when test="${empty vehicles}">

                    <div class="empty-message">
                        Nessun veicolo disponibile.
                    </div>

                </c:when>

                <c:otherwise>

                    <div class="vehicle-container">

                        <c:forEach var="vehicle"
                                   items="${vehicles}">

                            <%--
                                L'intera card è un collegamento
                                alla pagina di dettaglio.
                            --%>
                            <a class="vehicle-card"
                               href="${pageContext.request.contextPath}/vehicle?id=${vehicle.id}">

                                <c:choose>

                                    <c:when test="${not empty vehicle.imagePaths}">

                                        <img class="vehicle-card-image"
                                             src="${pageContext.request.contextPath}/${vehicle.imagePaths[0]}"
                                             alt="${vehicle.brand} ${vehicle.model}">

                                    </c:when>

                                    <c:otherwise>

                                        <div class="vehicle-image-placeholder">
                                            Nessuna immagine
                                        </div>

                                    </c:otherwise>

                                </c:choose>

                                <h2>
                                        ${vehicle.brand} ${vehicle.model}
                                </h2>

                                <p>Anno: ${vehicle.productionYear}</p>
                                <p>Km: ${vehicle.km}</p>
                                <p>Carburante: ${vehicle.fuelType}</p>
                                <p>Cambio: ${vehicle.transmission}</p>
                                <p>Prezzo: € ${vehicle.price}</p>

                            </a>

                        </c:forEach>

                    </div>

                </c:otherwise>

            </c:choose>

        </c:otherwise>

    </c:choose>

</main>

</body>
</html>