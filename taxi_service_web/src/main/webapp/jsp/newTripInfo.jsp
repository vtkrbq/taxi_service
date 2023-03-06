<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <title>Taxi Service</title>
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
                        <a class="mainmenubtn" href="${pageContext.request.contextPath}/logout">Sign out</a>
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
                        <b style="font-size: 32px; color: black;">You have successfully ordered taxi</b>
                    </div>
                </caption>
                <tr>
                    <td style="width: 300px;">Departure address:</td>
                    <td style="width: 500px;">${tripOrder.departure.address}</td>
                </tr>
                <tr>
                    <td>Destination address:</td>
                    <td>${tripOrder.destination.address}</td>
                </tr>
                <tr>
                    <td>Category:</td>
                    <td>${tripOrder.category}</td>
                </tr>
                <tr>
                    <td>Capacity:</td>
                    <td>${tripOrder.capacity}</td>
                </tr>
                <tr>
                    <td>Order time:</td>
                    <td>
                        <fmt:parseDate value="${tripOrder.timestampCreated}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
                        <fmt:formatDate pattern="HH:mm dd MMM yyyy" value="${parsedDateTime}" />
                    </td>
                </tr>
                <tr>
                    <td>Car:</td>
                    <td>${newTripInfo.car.carName}</td>
                </tr>
                <tr>
                    <td>Driver:</td>
                    <td>${newTripInfo.car.driver.firstname} ${newTripInfo.car.driver.lastname}</td>
                </tr>
                <tr>
                    <td>Estimated time arrival:</td>
                    <td>${newTripInfo.eta} min</td>
                </tr>
                <tr>
                    <td>Price:</td>
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