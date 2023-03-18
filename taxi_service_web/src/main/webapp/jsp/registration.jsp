<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" errorPage="error.jsp"%>
<c:if test="${empty lang}">
    <c:set var="lang" scope="session" value="en"/>
</c:if>
<fmt:setBundle basename="${sessionScope.lang}"/>
<fmt:requestEncoding value="UTF-8" />
<html>
<head>
    <title><fmt:message key="site.name" /></title>
    <link rel="stylesheet" type="text/css" href="css/style.css" />
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Exo+2:wght@300&display=swap" rel="stylesheet">
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
</head>
<body>
<h2><fmt:message key="reg.label" /></h2>
<div class="sidenav">
    <div style="padding-left: 10px;">
        <a href="${pageContext.request.contextPath}/language?lang=ua"><img src="img/ua.png" alt="ua language" style="width: 25px; height:25px; border-radius: 25px;"></a>
        <img src="img/slash.png" alt="logo alt" style="width: 1px; height:25px;">
        <a href="${pageContext.request.contextPath}/language?lang=en"><img src="img/en.png" alt="en language" style="width: 25px; height:25px; border-radius: 25px;"></a>
    </div>
    <div class="login-main-text">
        <p><fmt:message key="reg.welcome.label" /></p>
        <img src="img/main.png" style="width: 580px; height: 580px; margin-left: 5%;" alt="Taxi service">
    </div>
</div>
<div class="main">
    <div class="col-md-6 col-sm-12">
        <div class="register-form">
            <form action="registration" method="post">
                <div class="form-group">
                    <label><fmt:message key="login.label" /></label>
                    <fmt:message key="login.placeholder" var="loginPh"/>
                    <input type="text" name="login" class="form-control" placeholder="${loginPh}" value="${user.login}">
                </div>
                <div class="form-group">
                    <label><fmt:message key="password.label" /></label>
                    <fmt:message key="password.placeholder" var="passwordPh"/>
                    <input type="password" name="password" class="form-control" placeholder="${passwordPh}" >
                </div>
                <div class="form-group">
                    <label><fmt:message key="conf.pass.label" /></label>
                    <fmt:message key="conf.password.placeholder" var="confPasswordPh"/>
                    <input type="password" name="confirmPassword" class="form-control" placeholder="${confPasswordPh}">
                </div>
                <div class="form-group">
                    <label><fmt:message key="name.label" /></label>
                    <fmt:message key="name.placeholder" var="namePh"/>
                    <input type="text" name="firstname" class="form-control" placeholder="${namePh}" value="${user.firstname}">
                </div>
                <div class="form-group">
                    <label><fmt:message key="lastname.label" /></label>
                    <fmt:message key="lastname.placeholder" var="lastnamePh"/>
                    <input type="text" name="lastname" class="form-control" placeholder="${lastnamePh}" value="${user.lastname}">
                </div>
                <div class="form-group">
                    <label><fmt:message key="phone.label" /></label>
                    <fmt:message key="phone.placeholder" var="phonePh"/>
                    <input type="text" name="phone" class="form-control" placeholder="${phonePh}" value="${user.phone}">
                </div>
                <div class="form-group">
                    <label><fmt:message key="email.label" /></label>
                    <fmt:message key="email.placeholder" var="emailPh"/>
                    <input type="text" name="email" class="form-control" placeholder="${emailPh}" value="${user.email}">
                </div>
                <div class="form-group">
                    <input type="checkbox" name="registerAsDriver">
                    <label><fmt:message key="reg.as.driver.label" /></label>
                </div>
                <button type="submit" class="btn btn-black"><fmt:message key="reg.button" /></button>
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
<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
<script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
</body>
</html>
