<%--
  Created by IntelliJ IDEA.
  User: gemel
  Date: 02/07/2026
  Time: 16:09
  To change this template use File | Settings | File Templates.
--%>


<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head>
  <meta charset="UTF-8">
    <title>I miei ordini</title>

  <link rel="stylesheet"
  href="${pageContext.request.contextPath}/css/style.css">

</head>

<body>

<jsp:include page="/jsp/header.jsp" />

<div class="orders-container">
   <h1>I miei ordini</h1>

  <c:choose>

    <c:when test="${empty orders}">
      <p>Non hai ancora effettuato ordini.</p>
    </c:when>

    <c:otherwise>
      <c:forEach var = "order" items="${orders}">

        <div class="order-card">

          <h2>Ordine n. ${order.id}</h2>

          <p>
            <strong>Data:</strong>
            ${order.formattedOrderDate}
          </p>

          <p>
            <strong>Stato:</strong>
            ${order.status}
          </p>

          <p>
          <strong>Totale:</strong>
            € ${order.totalPrice}
          </p>


          <h3>Veicoli</h3>

          <c:forEach var="item"
          items="${order.items}">

            <div class="order-item">

              <span>
                ${item.vehicle.brand}
                ${item.vehicle.model}
              </span>

              <strong> € ${item.purchasePrice}</strong>
            </div>
          </c:forEach>

        </div>
      </c:forEach>
    </c:otherwise>
  </c:choose>
</div>

</body>
</html>
