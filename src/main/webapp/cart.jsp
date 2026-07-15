<%--
  Created by IntelliJ IDEA.
  User: gemel
  Date: 21/06/2026
  Time: 21:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
    <title>Carrello</title>

  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/css/style.css">

  <script src="${pageContext.request.contextPath}/js/cart.js"
          defer></script>

</head>

<body>
<jsp:include page="/jsp/header.jsp"/>

<main id="cart-page"
      class="cart-container"
      data-context-path="${pageContext.request.contextPath}">

  <h1>Il tuo carrello</h1>

  <!--
      Eventuale messaggio prodotto dalla CheckoutServlet,
      per esempio quando non è stato selezionato alcun veicolo.
  -->
  <c:if test="${not empty checkoutError}">

    <div class="form-errors"
         role="alert">

      <c:out value="${checkoutError}"/>

    </div>

  </c:if>

  <c:choose>

    <%-- Carrello inesistente oppure senza elementi --%>
    <c:when test="${empty sessionScope.cart
                        or empty sessionScope.cart.items}">

      <section class="empty-message">

        <h2>Il carrello è vuoto</h2>

        <p>
          Non hai ancora aggiunto nessun veicolo.
        </p>

        <a class="details-button"
           href="${pageContext.request.contextPath}/catalog">

          Vai al catalogo

        </a>

      </section>

    </c:when>

    <%-- Carrello con almeno un elemento --%>
    <c:otherwise>

      <section class="cart-items">

        <c:forEach var="item"
                   items="${sessionScope.cart.items}">

          <%--
              Creiamo gli URL con c:url e c:param.
              In questo modo i parametri vengono
              costruiti correttamente.
          --%>
          <c:url var="vehicleDetailsUrl"
                 value="/vehicle">

            <c:param name="id"
                     value="${item.vehicle.id}"/>

          </c:url>

          <c:url var="removeFromCartUrl"
                 value="/remove-from-cart">

            <c:param name="id"
                     value="${item.vehicle.id}"/>

          </c:url>

          <article class="cart-item">

            <div class="cart-item-selection">

              <label class="cart-selection-control">

                  <%--
                      La checkbox contiene l'ID del veicolo
                      in un attributo data-*.

                      JavaScript lo leggerà tramite:

                      checkbox.dataset.vehicleId
                  --%>
                <input type="checkbox"
                       class="cart-selection-checkbox"
                       data-vehicle-id="${item.vehicle.id}"
                  ${item.selected
                          ? 'checked'
                          : ''}>

                <span>
                                    Seleziona per l'acquisto
                                </span>

              </label>

            </div>

            <div class="cart-item-content">

              <h2>

                <a href="${vehicleDetailsUrl}">

                  <c:out value="${item.vehicle.brand}"/>

                  <c:out value="${item.vehicle.model}"/>

                </a>

              </h2>

              <div class="cart-item-information">

                <p>
                  <strong>Anno:</strong>

                  <c:out value="${item.vehicle.productionYear}"/>
                </p>

                <p>
                  <strong>Chilometri:</strong>

                  <fmt:formatNumber
                          value="${item.vehicle.km}"
                          type="number"
                          maxFractionDigits="0"/>

                  km
                </p>

                <p>
                  <strong>Alimentazione:</strong>

                  <c:out value="${item.vehicle.fuelType}"/>
                </p>

                <p>
                  <strong>Cambio:</strong>

                  <c:out value="${item.vehicle.transmission}"/>
                </p>

                <p class="cart-item-price">

                  <strong>Prezzo:</strong>

                  €

                  <fmt:formatNumber
                          value="${item.price}"
                          type="number"
                          minFractionDigits="2"
                          maxFractionDigits="2"/>

                </p>

              </div>

              <div class="cart-item-actions">

                <a class="details-button"
                   href="${vehicleDetailsUrl}">

                  Visualizza veicolo

                </a>

                  <%--
                      Questa versione usa un collegamento GET
                      perché RemoveFromCartServlet attualmente
                      gestisce la rimozione tramite doGet().
                  --%>
                <a class="details-button"
                   href="${removeFromCartUrl}">

                  Rimuovi dal carrello

                </a>

              </div>

            </div>

          </article>

        </c:forEach>

      </section>

      <aside class="cart-summary">

        <h2>Riepilogo</h2>

          <%--
              aria-live comunica ai lettori di schermo che
              questi valori possono cambiare dinamicamente.
          --%>
        <div aria-live="polite">

          <p>
            Veicoli selezionati:

            <strong id="selected-count">

              <c:out
                      value="${sessionScope.cart.selectedCount}"/>

            </strong>
          </p>

          <p class="cart-selected-total">

            Totale selezionato:

            <strong>
              €

              <span id="selected-total">

                                <fmt:formatNumber
                                        value="${sessionScope.cart.selectedTotal}"
                                        type="number"
                                        minFractionDigits="2"
                                        maxFractionDigits="2"/>

                            </span>
            </strong>

          </p>

        </div>

          <%--
              Non inviamo gli ID dei veicoli selezionati.

              Il server recupererà la selezione direttamente
              dal Cart salvato nella sessione.
          --%>
        <form action="${pageContext.request.contextPath}/checkout"
              method="post"
              class="checkout-form">

          <button id="checkout-button"
                  type="submit"
                  class="details-button"
            ${sessionScope.cart.selectedCount == 0
                    ? 'disabled'
                    : ''}>

            Acquista i veicoli selezionati

          </button>

        </form>

        <a class="continue-shopping-link"
           href="${pageContext.request.contextPath}/catalog">

          Continua gli acquisti

        </a>

      </aside>

    </c:otherwise>

  </c:choose>

</main>

</body>
</html>
