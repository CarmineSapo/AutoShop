<%@ page contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8"
         language="java" %>

<%@ taglib prefix="c"
           uri="jakarta.tags.core" %>

<%@ taglib prefix="fmt"
           uri="jakarta.tags.fmt" %>

<!DOCTYPE html>

<html lang="it">

<head>

  <meta charset="UTF-8">

  <meta name="viewport"
        content="width=device-width, initial-scale=1.0">

  <title>
    Profilo dealer
  </title>

  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/css/style.css">

</head>

<body>

<jsp:include page="/jsp/header.jsp"/>

<main class="dealer-public-profile">

  <section class="dealer-profile-header">

    <div class="dealer-profile-image">

      <div class="dealer-image-placeholder">
        <c:out value="${dealer.companyName.substring(0, 1)}"/>
      </div>

    </div>

    <div class="dealer-profile-information">

      <h1>
        <c:out value="${dealer.companyName}"/>
      </h1>

      <c:if test="${not empty dealer.description}">

        <p class="dealer-description">
          <c:out value="${dealer.description}"/>
        </p>

      </c:if>

      <p>
        <strong>Telefono:</strong>

        <c:out value="${dealer.phone}"/>
      </p>

      <p>
        <strong>Indirizzo:</strong>

        <c:out value="${dealer.address}"/>
      </p>

    </div>

  </section>

  <section class="dealer-posts-section">

    <h2>Veicoli pubblicati</h2>

    <c:choose>

      <c:when test="${empty vehicles}">

        <p class="empty-message">
          Questo dealer non ha veicoli disponibili.
        </p>

      </c:when>

      <c:otherwise>

        <div class="dealer-post-grid">

          <c:forEach var="vehicle"
                     items="${vehicles}">

            <%--
                L'intera card è cliccabile e porta
                al dettaglio del veicolo.
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
                <c:out value="${vehicle.brand}"/>
                <c:out value="${vehicle.model}"/>
              </h2>

              <p>
                <strong>Anno:</strong>
                <c:out value="${vehicle.productionYear}"/>
              </p>

              <p>
                <strong>Chilometri:</strong>

                <fmt:formatNumber
                        value="${vehicle.km}"
                        type="number"
                        maxFractionDigits="0"/>

                km
              </p>

              <p>
                <strong>Prezzo:</strong>

                €

                <fmt:formatNumber
                        value="${vehicle.price}"
                        type="number"
                        minFractionDigits="2"
                        maxFractionDigits="2"/>
              </p>

            </a>

          </c:forEach>

        </div>

      </c:otherwise>

    </c:choose>

  </section>

</main>

</body>

</html>