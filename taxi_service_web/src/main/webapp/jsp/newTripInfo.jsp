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
    <link rel="stylesheet" type="text/css" href="css/header.css" />
    <link rel="stylesheet" type="text/css" href="css/ordering.css" />
    <link rel="stylesheet" type="text/css" href="css/style.css" />
    <link rel="stylesheet" type="text/css" href="css/table.css" />
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Exo+2:wght@300&display=swap" rel="stylesheet">
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
</head>
<body>
<header class="top-line">
		<a href="#" class="logo"><img src="img/logo.png" alt="logo alt"></a>
		<div class="login">
		    <i class="fa fa-login"></i>
		    <div class="dropdown">
                <button class="mainmenubtn"><c:out value="${currentUser.firstname} ${currentUser.lastname}"/></button>
                    <div class="dropdown-child">
                        <a class="mainmenubtn" href="${pageContext.request.contextPath}/logout"><fmt:message key="ddm.logout.button" /></a>
                    </div>
            </div>
		</div>
</header>
<div class="page-wrapper">
    <div class="left-panel-wrapper">
        <div class="left-panel">
            <table class="table" style="font-size: 20px; padding: 5px; font-family: Exo 2; width: 500px;">
                <caption>
                    <div style="padding-left: 0px; display: inline-block;">
                        <b style="font-size: 32px; color: black;"><fmt:message key="success.label" /></b>
                    </div>
                </caption>
                <tr>
                    <td style="width: 300px;"><fmt:message key="success.label" />:</td>
                    <td style="width: 500px;">${tripOrder.departure.address}</td>
                </tr>
                <tr>
                    <td><fmt:message key="depart.address.field" />:</td>
                    <td>${tripOrder.destination.address}</td>
                </tr>
                <tr>
                    <td><fmt:message key="destin.address.field" />:</td>
                    <td>${tripOrder.category}</td>
                </tr>
                <tr>
                    <td><fmt:message key="category.field" />:</td>
                    <td>${tripOrder.capacity}</td>
                </tr>
                <tr>
                    <td><fmt:message key="capacity.field" />:</td>
                    <td>
                        <fmt:parseDate value="${tripOrder.timestampCreated}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
                        <fmt:formatDate pattern="HH:mm dd MMM yyyy" value="${parsedDateTime}" />
                    </td>
                </tr>
                <tr>
                    <td><fmt:message key="car.field" />:</td>
                    <td>${newTripInfo.car.carName}</td>
                </tr>
                <tr>
                    <td><fmt:message key="driver.label" />:</td>
                    <td>${newTripInfo.car.driver.firstname} ${newTripInfo.car.driver.lastname}</td>
                </tr>
                <tr>
                    <td><fmt:message key="eta.label" />:</td>
                    <td>${newTripInfo.eta} min</td>
                </tr>
                <tr>
                    <td><fmt:message key="price.field" />:</td>
                    <td>${newTripInfo.price} ₴</td>
                </tr>
            </table>
        </div>
</div>

<div class="content-wrapper">
    <div class="content">
    </div>
</div>
</div>
<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
</body>
</html>