<%@ page contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8"
         language="java" %>

<%@ taglib prefix="c"
           uri="jakarta.tags.core" %>

<header class="site-header">

    <a class="site-logo"
       href="${pageContext.request.contextPath}/">
        AutoShop
    </a>

    <nav class="site-navigation"
         aria-label="Navigazione principale">

        <a href="${pageContext.request.contextPath}/">
            Home
        </a>

        <a href="${pageContext.request.contextPath}/catalog">
            Filtri
        </a>

        <a href="${pageContext.request.contextPath}/cart">
            Carrello
        </a>

        <c:choose>

            <%--
                visitatore non autenticato viene portato
                alla pagina di login
            --%>
            <c:when test="${empty sessionScope.user}">

                <a href="${pageContext.request.contextPath}/login.jsp">
                    Profilo
                </a>

            </c:when>

            <%--
                 utente autenticato apre il proprio profilo
            --%>
            <c:otherwise>

                <a href="${pageContext.request.contextPath}/profile">
                    Profilo
                </a>

            </c:otherwise>

        </c:choose>

    </nav>

</header>