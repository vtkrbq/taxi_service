<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<c:if test="${empty lang}">
    <c:set var="lang" scope="request" value="en"/>
</c:if>
<fmt:setBundle basename="${lang}"/>
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
        <p><fmt:message key="welcome.label" /></p>
        <img src="img/main.png" style="width: 550px; height: 550px; display: flex; justify-content: center;" alt="Taxi service">
    </div>
</div>
<div class="main">
    <div class="col-md-6 col-sm-12">
        <div class="login-form">
            <form action="login" method="post">
                <div class="form-group">
                    <label><fmt:message key="login.label" /></label>
                    <fmt:message key="login.placeholder" var="login_placeholder" />
                    <input type="text" name="login" class="form-control" placeholder="${login_placeholder}">
                </div>
                <div class="form-group">
                    <label><fmt:message key="password.label" /></label>
                    <fmt:message key="password.placeholder" var="password_placeholder" />
                    <input type="password" name="password" class="form-control" placeholder="${password_placeholder}">
                </div>
                    <button type="submit" class="btn btn-black"><fmt:message key="login.button" /></button>
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/registration"><fmt:message key="index.reg.button" /></a>
                    <c:if test="${errors != null && not empty errors}">
                        <div style="color: red">
                            <c:forEach items="${errors}" var="error" >
                                <div><c:out value="${error}"/></div>
                            </c:forEach>
                        </div>
                    </c:if>
            </form>
        </div>
    </div>
</div>
</body>
</html>