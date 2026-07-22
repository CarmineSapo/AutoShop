<%--
  Created by IntelliJ IDEA.
  User: gemel
  Date: 14/07/2026
  Time: 18:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"
         language="java" %>

<%@ taglib prefix="c"
           uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">

    <title>Aggiungi veicolo</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">
</head>

<body>

<jsp:include page="/jsp/header.jsp"/>

<main class="vehicle-form-container">

    <h1>Aggiungi un veicolo</h1>

    <c:if test="${not empty errors}">

        <div class="form-errors">

            <p>
                Correggi i seguenti errori:
            </p>

            <ul>
                <c:forEach var="error"
                           items="${errors}">

                    <li>
                        <c:out value="${error}"/>
                    </li>

                </c:forEach>
            </ul>

        </div>

    </c:if>

    <form action="${pageContext.request.contextPath}/dealer/add-vehicle"
          method="post"
          enctype="multipart/form-data"
          class="vehicle-form">

        <div class="form-group">
            <label for="brand">
                Marca
            </label>

            <input type="text"
                   id="brand"
                   name="brand"
                   maxlength="50"
                   value="<c:out value='${param.brand}' />"
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
                   value="<c:out value='${param.model}' />"
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
                   value="<c:out value='${param.productionYear}' />"
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
                   value="<c:out value='${param.km}' />"
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

                <option value="MANUALE"
                ${param.transmission eq 'MANUALE'
                        ? 'selected' : ''}>
                    Manuale
                </option>

                <option value="AUTOMATICA"
                ${param.transmission eq 'AUTOMATICA'
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
                   value="<c:out value='${param.price}' />"
                   required>
        </div>

        <div class="form-group">
            <label for="description">
                Descrizione
            </label>

            <textarea id="description"
                      name="description"
                      rows="6"
                      maxlength="2000"><c:out value="${param.description}"/></textarea>
        </div>

        <div class="form-group">

            <label for="images">
                Immagini del veicolo
            </label>

            <input type="file"
                   id="images"
                   name="images"
                   accept="image/jpeg,image/png,image/webp"
                   multiple>

            <small>
                Puoi caricare un massimo di 3 immagini.
            </small>

        </div>

        <div class="vehicle-form-actions">

            <button type="submit"
                    class="details-button">
                Pubblica veicolo
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