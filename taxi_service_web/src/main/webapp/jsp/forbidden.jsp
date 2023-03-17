<%@ page isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<c:if test="${empty lang}">
    <c:set var="lang" scope="session" value="en"/>
</c:if>
<fmt:setBundle basename="${sessionScope.lang}"/>
<fmt:requestEncoding value="UTF-8" />
<html>
<head>
    <title><fmt:message key="site.name" /></title>
    <link rel="stylesheet" type="text/css" href="css/style.css" />
    <link rel="stylesheet" type="text/css" href="css/header.css" />
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
    <script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
</head>
<body>
<fmt:requestEncoding value="UTF-8" />
<h2 style="text-align: center; padding-top: 5%;"><a href="${pageContext.request.contextPath}"><fmt:message key="the.one.label" /></a></h2>
</body>
</html>