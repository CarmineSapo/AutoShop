<%--
  Created by IntelliJ IDEA.
  User: gemel
  Date: 21/06/2026
  Time: 19:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8"
         language="java" %>

<%@ taglib prefix="c"
           uri="jakarta.tags.core" %>

<!DOCTYPE html>

<html lang="it">

<head>

  <meta charset="UTF-8">

  <meta name="viewport"
        content="width=device-width, initial-scale=1.0">

  <title>Profilo</title>

  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/css/style.css">

</head>

<body>

<jsp:include page="/jsp/header.jsp"/>

<main class="form-card">

  <h1>Profilo utente</h1>

  <p>
    <strong>Username:</strong>

    <c:out value="${sessionScope.user.username}"/>
  </p>

  <p>
    <strong>Email:</strong>

    <c:out value="${sessionScope.user.email}"/>
  </p>

  <p>
    <strong>Ruolo:</strong>

    <c:out value="${sessionScope.user.role}"/>
  </p>

  <%--
      Il form per diventare dealer viene mostrato
      soltanto agli utenti con ruolo CUSTOMER.
  --%>
  <c:if test="${sessionScope.user.role eq 'CUSTOMER'}">

    <section class="upgrade-dealer-section">

      <h2>Diventa dealer</h2>

      <p>
        Inserisci i dati della tua concessionaria
        per iniziare a pubblicare veicoli.
      </p>

        <%--
            Gli errori vengono inseriti nella request
            dalla UpgradeToDealerServlet.
        --%>
      <c:if test="${not empty upgradeErrors}">

        <div class="form-errors"
             role="alert">

          <p>
            Correggi i seguenti errori:
          </p>

          <ul>

            <c:forEach var="error"
                       items="${upgradeErrors}">

              <li>
                <c:out value="${error}"/>
              </li>

            </c:forEach>

          </ul>

        </div>

      </c:if>

      <form action="${pageContext.request.contextPath}/upgrade-to-dealer"
            method="post"
            class="vehicle-form">

        <div class="form-group">

          <label for="companyName">
            Nome concessionaria
          </label>

          <input type="text"
                 id="companyName"
                 name="companyName"
                 maxlength="100"
                 value="${param.companyName}"
                 required>

        </div>

        <div class="form-group">

          <label for="vatNumber">
            Partita IVA
          </label>

          <input type="text"
                 id="vatNumber"
                 name="vatNumber"
                 pattern="[0-9]{11}"
                 minlength="11"
                 maxlength="11"
                 inputmode="numeric"
                 value="${param.vatNumber}"
                 required>

          <small>
            Inserisci esattamente 11 cifre.
          </small>

        </div>

        <div class="form-group">

          <label for="phone">
            Telefono
          </label>

          <input type="tel"
                 id="phone"
                 name="phone"
                 maxlength="20"
                 value="${param.phone}"
                 required>

        </div>

        <div class="form-group">

          <label for="address">
            Indirizzo
          </label>

          <input type="text"
                 id="address"
                 name="address"
                 maxlength="255"
                 value="${param.address}"
                 required>

        </div>

        <div class="form-group">

          <label for="description">
            Descrizione
          </label>

          <textarea id="description"
                    name="description"
                    maxlength="1000"
                    rows="5">${param.description}</textarea>

        </div>

        <button type="submit"
                class="details-button">

          Diventa dealer

        </button>

      </form>

    </section>

  </c:if>

</main>

</body>

</html>