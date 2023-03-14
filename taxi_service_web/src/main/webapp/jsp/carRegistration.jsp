<%@ page isELIgnored="false" %>
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
    <link rel="stylesheet" type="text/css" href="css/select.css" />
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Exo+2:wght@300&display=swap" rel="stylesheet">
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <title>Welcome</title>
</head>
<body>
<div class="sidenav">
    <div class="login-main-text">
        <p><fmt:message key="welcome.label" /></p>
        <img src="img/main.png" style="width: 600px; height: 600px;" alt="Taxi service">
    </div>
</div>
<div class="main">
    <div class="col-md-6 col-sm-12">
        <div class="register-form">
            <form action="${pageContext.request.contextPath}/carRegistration" method="post">
                <div class="form-group">
                    <label><fmt:message key="car.label" /></label>
                    <fmt:message key="car.placeholder" var="car_placeholder"/>
                    <input type="text" name="carName" class="form-control" placeholder="${car_placeholder}" value="${car.carName}">
                </div>
                <div class="form-group">
                    <label><fmt:message key="car.сategory.label" /></label>
                    <div class="select">
                        <select name="carCategory" value="${car.carCategory}">
                            <option>Economy</option>
                            <option>Comfort</option>
                            <option>Business</option>
                        </select>
                        <span class="focus"></span>
                    </div>
                </div>
                <div class="form-group">
                    <label><fmt:message key="car.сapacity.label" /></label>
                    <fmt:message key="car.сapacity.placeholder" var="car_сapacity_placeholder" />
                    <input type="text" name="carCapacity" class="form-control" placeholder="${car_сapacity_placeholder}" value="${car.carCapacity}">
                </div>
                <div class="form-group">
                    <label><fmt:message key="car.license.plate.label" /></label>
                    <fmt:message key="car.license.plate.placeholder" var="car_license_plate_placeholder"/>
                    <input type="text" name="licensePlate" class="form-control" placeholder="${car_license_plate_placeholder}" value="${car.licensePlate}">
                </div>
                <button type="submit" class="btn btn-black"><fmt:message key="reg.car.button" /></button>
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
