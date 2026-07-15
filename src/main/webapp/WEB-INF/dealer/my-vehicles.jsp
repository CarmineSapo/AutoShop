<%--
  Created by IntelliJ IDEA.
  User: gemel
  Date: 13/07/2026
  Time: 21:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>My vehicles</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<jsp:include page="/jsp/header.jsp"/>

<div class="vehicle-dealer-container">

    <h1>I miei veicoli</h1>

    <div class="dealer-vehicles-actions">
        <a class="details-button"
           href="${pageContext.request.contextPath}/dealer/add-vehicle">
            Aggiungi veicolo
        </a>

        <a class="details-button"
           href="${pageContext.request.contextPath}/dealer/dashboard">
            Trona alla dashboard
        </a>

    </div>

    <c:choose>

        <c:when test="${empty vehicles}">
            <p class="empty-message">
                Non hai ancora pubblicato veicoli.
            </p>
        </c:when>


        <c:otherwise>

            <div class="vehicle-container">

                <c:forEach var="vehicle" items="${vehicles}">

                    <div class="vehicle-card">

                        <h2>
                                ${vehicle.brand} ${vehicle.model}
                        </h2>

                        <p>
                            <strong>Anno:</strong>
                                ${vehicle.productionYear}
                        </p>

                        <p>
                            <strong>Chilometri:</strong>
                                ${vehicle.km}
                        </p>

                        <p>
                            <strong>Stato:</strong>
                                ${vehicle.status}
                        </p>

                        <p>
                            <strong>Visibilità:</strong>

                            <c:choose>
                                <c:when test="${vehicle.active}">
                                    Attivo
                                </c:when>

                                <c:otherwise>
                                    Disattivato
                                </c:otherwise>
                            </c:choose>
                        </p>


                        <div class="vehicle-card-actions">

                            <c:if test="${vehicle.active}">
                                <a class="details-button"
                                   href="${pageContext.request.contextPath}/vehicle?id=${vehicle.id}">
                                    Dettagli
                                </a>
                            </c:if>
                            <a class="details-button"
                               href="${pageContext.request.contextPath}/dealer/edit-vehicle?id=${vehicle.id}">
                                Modifica
                            </a>

                            <c:choose>

                                <c:when test="${vehicle.active}">

                                    <form action="${pageContext.request.contextPath}/dealer/change-vehicle-visibility"
                                          method="post"
                                          class="inline-form">

                                        <input type="hidden"
                                               name="id"
                                               value="<c:out value='${vehicle.id}' />">

                                        <input type="hidden"
                                               name="action"
                                               value="deactivate">

                                        <button type="submit"
                                                class="details-button">
                                            Disattiva
                                        </button>

                                    </form>

                                </c:when>

                                <c:otherwise>

                                    <form action="${pageContext.request.contextPath}/dealer/change-vehicle-visibility"
                                          method="post"
                                          class="inline-form">

                                        <input type="hidden"
                                               name="id"
                                               value="<c:out value='${vehicle.id}' />">

                                        <input type="hidden"
                                               name="action"
                                               value="activate">

                                        <button type="submit"
                                                class="details-button">
                                            Riattiva
                                        </button>

                                    </form>

                                </c:otherwise>

                            </c:choose>


                        </div>

                    </div>

                </c:forEach>

            </div>
        </c:otherwise>
    </c:choose>


</div>

</body>
</html>
