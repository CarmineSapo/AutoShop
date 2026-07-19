<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Modifica veicolo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<jsp:include page="/jsp/header.jsp"/>

<main class="vehicle-form-container">

    <h1>Modifica veicolo</h1>

    <c:if test="${not empty errors}">

        <div class="form-errors">

            <p>Correggi i seguenti errori:</p>

            <ul>
                <c:forEach var="error" items="${errors}">

                    <li>
                        <c:out value="${error}"></c:out>
                    </li>

                </c:forEach>

            </ul>

        </div>

    </c:if>


    <form action="${pageContext.request.contextPath}/dealer/edit-vehicle"
          method="post"
          class="vehicle-form">

        <input type="hidden"
               name="id"
               value="<c:out value='${vehicle.id}' />">

        <div class="form-group">

            <label for="brand">Marca</label>

            <input type="text"
                   id="brand"
                   name="brand"
                   maxlength="50"
                   value="<c:out value='${vehicle.brand}' />"
                   required>

        </div>

        <div class="form-group">

            <label for="model">
                Modello
            </label>

            <input type="text"
                   id="model"
                   name="model"
                   maxlength="50"
                   value="<c:out value='${vehicle.model}' />"
                   required>

        </div>

        <div class="form-group">

            <label for="productionYear">
                Anno di produzione
            </label>

            <input type="number"
                   id="productionYear"
                   name="productionYear"
                   min="1886"
                   max="2026"
                   value="<c:out value='${vehicle.productionYear}' />"
                   required>

        </div>

        <div class="form-group">

            <label for="km">
                Chilometri
            </label>

            <input type="number"
                   id="km"
                   name="km"
                   min="0"
                   value="<c:out value='${vehicle.km}' />"
                   required>

        </div>

        <div class="form-group">

            <label for="fuelType">
                Alimentazione
            </label>

            <select id="fuelType"
                    name="fuelType"
                    required>

                <option value=""
                        disabled>
                    Seleziona
                </option>

                <option value="Benzina"
                ${vehicle.fuelType eq 'Benzina'
                        ? 'selected' : ''}>
                    Benzina
                </option>

                <option value="Diesel"
                ${vehicle.fuelType eq 'Diesel'
                        ? 'selected' : ''}>
                    Diesel
                </option>

                <option value="Elettrico"
                ${vehicle.fuelType eq 'Elettrico'
                        ? 'selected' : ''}>
                    Elettrico
                </option>

                <option value="Ibrido"
                ${vehicle.fuelType eq 'Ibrido'
                        ? 'selected' : ''}>
                    Ibrido
                </option>

                <option value="GPL"
                ${vehicle.fuelType eq 'GPL'
                        ? 'selected' : ''}>
                    GPL
                </option>

            </select>

        </div>

        <div class="form-group">

            <label for="transmission">
                Cambio
            </label>

            <select id="transmission"
                    name="transmission"
                    required>

                <option value=""
                        disabled>
                    Seleziona
                </option>

                <option value="Manuale"
                ${vehicle.transmission eq 'Manuale'
                        ? 'selected' : ''}>
                    Manuale
                </option>

                <option value="Automatico"
                ${vehicle.transmission eq 'Automatico'
                        ? 'selected' : ''}>
                    Automatico
                </option>

            </select>

        </div>




        <div class="form-group">

            <label for="price">
                Prezzo
            </label>

            <input type="number"
                   id="price"
                   name="price"
                   min="0.01"
                   step="0.01"
                   value="<c:out value='${vehicle.price}' />"
                   required>

        </div>

        <div class="form-group">

            <label for="description">
                Descrizione
            </label>

            <textarea id="description"
                      name="description"
                      rows="6"
                      maxlength="2000"><c:out value="${vehicle.description}" /></textarea>

        </div>

        <div class="form-group">

            <label>Stato attuale</label>

            <p>
                <strong>
                    <c:out value="${vehicle.status}" />
                </strong>
            </p>

        </div>




        <div class="vehicle-form-actions">

            <button type="submit"
                    class="details-button">
                Salva modifiche
            </button>


            <a href="${pageContext.request.contextPath}/dealer/vehicles"
               class="details-button">
                Annulla
            </a>

        </div>

    </form>

</main>

</body>
</html>
