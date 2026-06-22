<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: gemel
  Date: 20/06/2026
  Time: 18:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<header class="site-header">

    <div class="header-left">
        <div class="logo">AutoShop</div>
    </div>

    <nav class="header-center">
        <a href="${pageContext.request.contextPath}/">Home</a>
        <a href="${pageContext.request.contextPath}/catalog">Catalogo</a>
        <a href="${pageContext.request.contextPath}/cart">Carrello</a>
    </nav>

    <div class="header-right">
        <c:choose>
            <c:when test="${empty sessionScope.user}">
                <a href="${pageContext.request.contextPath}/login.jsp">Login</a>
                <a href="${pageContext.request.contextPath}/register.jsp">Registrati</a>
            </c:when>

            <c:otherwise>
                <span class="user-greeting">Ciao, ${sessionScope.user.username}</span>

                <a href="${pageContext.request.contextPath}/profile">Profilo</a>

                <a href="${pageContext.request.contextPath}/logout">Logout</a>

            </c:otherwise>
        </c:choose>
    </div>

</header>
