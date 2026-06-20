<%@ page import="java.util.List" %>
<%@ page import="model.bean.Vehicle" %><%--
  Created by IntelliJ IDEA.
  User: gemel
  Date: 19/06/2026
  Time: 19:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>


<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Catalogo auto</title>
    <link rel="stylesheet"
           href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<jsp:include page="/jsp/header.jsp" />

<h1>Catalogo Auto</h1>


<c:choose>
    <c:when test="${empty vehicles}">
        <p>Nessun veicolo disponibile.</p>
    </c:when>

    <c:otherwise>

        <div class="vehicle-containter">

            <c:forEach var="vehicle" items="${vehicles}">
                <div class="vehicle-card">
                    <h2>${vehicle.brand} ${vehicle.model}</h2>
                    <p>Anno: ${vehicle.productionYear}</p>
                    <p>Km: ${vehicle.km}</p>
                    <p>Carburante: ${vehicle.fuelType}</p>
                    <p>Cambio: ${vehicle.transmission}</p>
                    <p>Prezzo: ${vehicle.price}</p>
                    <p>Descrizione: ${vehicle.description}</p>

                    <a  class="details-button"
                        href="${pageContext.request.contextPath}/vehicle?id=${vehicle.id}">

                        Vedi dettagli
                    </a>
                </div>
            </c:forEach>

        </div>

    </c:otherwise>
</c:choose>


</body>
</html>
