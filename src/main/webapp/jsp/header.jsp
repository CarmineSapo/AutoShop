<%@ page contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8"
         language="java" %>

<%@ taglib prefix="c"
           uri="jakarta.tags.core" %>

<header class="site-header">

    <a class="site-logo"
       href="${pageContext.request.contextPath}/catalog">
        AutoShop
    </a>

    <nav class="site-navigation"
         aria-label="Navigazione principale">

        <%--
            Home mostra direttamente la griglia
            dei veicoli disponibili.
        --%>
        <a href="${pageContext.request.contextPath}/catalog">
            Home
        </a>

        <%--
            Usa la stessa Servlet del catalogo,
            ma segnala alla JSP che deve mostrare
            anche il pannello dei filtri.
        --%>
        <a href="${pageContext.request.contextPath}/catalog?showFilters=true">
            Filtri
        </a>

        <a href="${pageContext.request.contextPath}/cart">
            Carrello
        </a>

        <c:choose>

            <c:when test="${empty sessionScope.user}">

                <a href="${pageContext.request.contextPath}/login.jsp">
                    Profilo
                </a>

            </c:when>

            <c:otherwise>

                <a href="${pageContext.request.contextPath}/profile">
                    Profilo
                </a>

            </c:otherwise>

        </c:choose>

    </nav>

</header>