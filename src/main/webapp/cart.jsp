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

</head>

<body>
<jsp:include page="/jsp/header.jsp"/>

<div class="form-card">

  <h1>Carrello</h1>

  <c:choose>
    <c:when test="${empty sessionScope.cart}">
      <p>Il carrello è vuoto.</p>
    </c:when>

    <c:when test="${empty sessionScope.cart.items}">
      <p>Il carrello è vuoto.</p>
    </c:when>

    <c:otherwise>
      <c:forEach var="item" items="${sessionScope.cart.items}">
        <div class="cart-item">
          <h3>${item.vehicle.brand} ${item.vehicle.model}</h3>
          <p>Prezzo: € ${item.price}</p>

          <a class="details-button"
             href="${pageContext.request.contextPath}/remove-from-cart?id=${item.vehicle.id}">
            Rimuovi
          </a>
        </div>
      </c:forEach>

      <hr>

      <h3>Totale: € ${sessionScope.cart.total}</h3>

      <form action="${pageContext.request.contextPath}/checkout"
            method="post">

        <button class="details-button" type="submit">
          Conferma ordine
        </button>
      </form>
    </c:otherwise>
  </c:choose>
</div>

</body>
</html>
