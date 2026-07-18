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

            <c:if test="${not empty filterError}">
                <p class="form-errors">
                    ${filterError}
                </p>
            </c:if>

            <form action="${pageContext.request.contextPath}/catalog"
                  method="get"
                  class="catalog-filters">

                <input type="hidden"
                       name="showFilters"
                       value="true">

                <div class="form-group">

                    <label for="brand">Marca</label>
                    <input type="text"
                           id="brand"
                           name="brand"
                           maxlength="50"
                           value="${param.brand}"

                </div>

                <div class="form-group">

                    <label for="maxPrice">Prezzo Massimo</label>
                    <input type="number"
                           id="maxPrice"
                           name="maxPrice"
                           min="1"
                           step="0.01"
                           value="${param.maxPrice}">

                </div>

                <div class="form-group">

                    <label for="fuelType">Carburante</label>
                    <select id="fuelType"
                            name="fuelType">

                        <option value="">
                            Tutti
                        </option>

                        <option value="Benzina"
                            ${param.fuelType eq 'Benzina'
                                    ? 'selected' : ''}>
                            Benzina
                        </option>

                        <option value="Diesel"
                            ${param.fuelType eq 'Diesel'
                                    ? 'selected' : ''}>
                            Diesel
                        </option>

                        <option value="Elettrico"
                            ${param.fuelType eq 'Elettrico'
                                    ? 'selected' : ''}>
                            Elettrico
                        </option>

                        <option value="Ibrido"
                            ${param.fuelType eq 'Ibrido'
                                    ? 'selected' : ''}>
                            Ibrido
                        </option>

                        <option value="GPL"
                            ${param.fuelType eq 'GPL'
                                    ? 'selected' : ''}>
                            GPL

                        </option>

                    </select>

                </div>

                <div class="form-group">

                    <label for="transmission">Cambio</label>

                    <select id="transmission"
                            name="transmission">

                        <option value="">
                            Tutti
                        </option>

                        <option value="Manuale"
                            ${param.transmission eq 'Manuale'
                                    ? 'selected' : ''}>
                            Manuale
                        </option>

                        <option value="Automatico"
                            ${param.transmission eq 'Automatico'
                                    ? 'selected' : ''}>
                            Automatico
                        </option>

                    </select>

                </div>

                <div class="filter-buttons">

                    <button type="submit"
                            class="details-button">
                        Applica filtri
                    </button>

                    <a class="details-button"
                       href="${pageContext.request.contextPath}/catalog?showFilters=true">
                        Rimuovi filtri
                    </a>

                </div>

            </form>

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