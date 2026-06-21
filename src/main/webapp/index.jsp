<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>

    <meta charset="UTF-8">
    <title>Autoshop</title>

    <link rel="stylesheet"
    href="${pageContext.request.contextPath}/css/style.css">

</head>

<body>

<jsp:include page="/jsp/header.jsp"/>

    <div class="hero">

        <h1>Benvenuto su Autoshop</h1>

        <P>
            Trova l'auto perfetta per te.
        </P>

        <a class="details-button"
           href="${pageContext.request.contextPath}/catalog">

            Vai al catalogo

        </a>

    </div>


</body>
</html>