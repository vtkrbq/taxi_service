<%@ page isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8"%>
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
<div class="sidenav">
    <div style="padding-left: 10px;">
        <a href="${pageContext.request.contextPath}/language?lang=ua"><img src="img/ua.png" alt="ua language" style="width: 25px; height:25px; border-radius: 25px;"></a>
        <img src="img/slash.png" alt="logo alt" style="width: 1px; height:25px;">
        <a href="${pageContext.request.contextPath}/language?lang=en"><img src="img/en.png" alt="en language" style="width: 25px; height:25px; border-radius: 25px;"></a>
    </div>
    <div class="login-main-text">
        <h3 style="text-align: center;"><fmt:message key="unathorized.label" /></h3>
        <img src="img/main.png" style="width: 580px; height: 580px; margin-left: 5%;" alt="Taxi service">
    </div>
</div>
<div class="main">
    <div style="padding-top: 40%;" >
        <h2><fmt:message key="please.label" />, <a href="${pageContext.request.contextPath}"><fmt:message key="login.message.label" /></a>, <fmt:message key="message.label" /></h2>
    </div>
</div>
</body>
</html>




