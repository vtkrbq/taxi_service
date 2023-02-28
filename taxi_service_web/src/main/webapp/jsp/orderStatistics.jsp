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
    <link rel="stylesheet" type="text/css" href="css/pagination.css" />
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Exo+2:wght@300&display=swap" rel="stylesheet">
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
</head>
<body>
<script>
      $('.top-line').after('<div class="mobile-menu d-xl-none">');
      	$('.top-menu').clone().appendTo('.mobile-menu');
      	$('.mobile-menu-btn').click(function(){
      		$('.mobile-menu').stop().slideToggle();
      	});
</script>
<header class="top-line">
		<a href="${pageContext.request.contextPath}/ordering" class="logo"><img src="img/logo.png" alt="logo alt"></a>
		<div class="login">
		    <i class="fa fa-login"></i>
		    <div class="dropdown">
                <button class="mainmenubtn"><c:out value="${currentUser.firstname} ${currentUser.lastname}"/></button>
                    <div class="dropdown-child">
                        <a class="mainmenubtn" href="${pageContext.request.contextPath}/profile">Profile</a>
                        <a class="mainmenubtn" href="${pageContext.request.contextPath}/logout">Sign out</a>
                    </div>
            </div>
		</div>
		<div class="mobile-menu-btn"><i class="fa fa-bars"></i> Меню</div>
		<nav class="main-menu top-menu">
        <ul>
            <li class="active"><a href="${pageContext.request.contextPath}/ordering">Order</a></li>
            <c:set var="admin" scope="request" value="false"/>
            <c:set var="driver" scope="request" value="false"/>
            <c:forEach items="${sessionScope.currentUser.roles}" var="userRole" >
                <c:if test="${userRole == 'ADMIN'}">
                    <c:set var="admin" scope="request" value="true"/>
                </c:if>
                <c:if test="${userRole == 'DRIVER'}">
                    <c:set var="driver" scope="request" value="true"/>
                </c:if>
            </c:forEach>
            <c:if test="${admin}">
                <li class="active"><a href="${pageContext.request.contextPath}/orderStatistics">Order statistics</a></li>
            </c:if>
            <c:if test="${driver}">
                <li class="active"><a href="${pageContext.request.contextPath}/ordering">DRIVER</a></li>
            </c:if>
        </ul>
		</nav>
</header>
<div class="container">
    <form action="orderStatistics">
        <div style="display: flex; justify-content: right;">
            <div style="display: flex; align-items: center;">
                <label style="margin: 5px; width: 200px; text-align: right;">Sort:</label>
                <select name="sortType" class="custom-select">
                  <option value="asc" selected>Ascending</option>
                  <option value="desc">Descending</option>
                </select>
            </div>
            <div style="display: flex; margin: 5px; align-items: center;">
                <label style="margin: 5px; width: 200px; text-align: right;">Sort by:</label>
                <select name="sortBy" class="custom-select">
                  <option value="departure_address" selected>Departure address</option>
                  <option value="destination_address">Destination address</option>
                  <option value="trip_category">Category</option>
                  <option value="trip_capacity">Capacity</option>
                  <option value="trip_order.lastname">Client name</option>
                  <option value="car.car_name">Car</option>
                  <option value="trip_order.price">Price</option>
                  <option value="trip_order.created">Created</option>
                </select>
            </div>
            <div style="display: flex; margin: 5px; align-items: center;">
                <label style="margin: 5px; width: 250px; text-align: right;">Filter by:</label>
                <select name="filterBy" class="custom-select" style="margin: 5px;">
                    <option value="departure_address" selected>Departure address</option>
                    <option value="destination_address">Destination address</option>
                    <option value="trip_category">Category</option>
                    <option value="trip_capacity">Capacity</option>
                    <option value="trip_order.lastname">Client name</option>
                    <option value="car.car_name">Car</option>
                    <option value="trip_order.price">Price</option>
                    <option value="trip_order.created">Created</option>
                </select>
                <input type="text" name="filterKey" class="form-control" placeholder="Filter key" style="margin: 5px;">
                <button type="submit" class="btn btn-black" style="width: 300px; margin: 5px;">Filter</button>
            </div>
        </div>
    <form>
    <table class="table table-striped table-class">
        <thead>
        <tr>
            <th>Departure address</th>
            <th>Destination address</th>
            <th>Category</th>
            <th>Capacity</th>
            <th>Client name</th>
            <th>Car</th>
            <th>Price</th>
            <th>Created</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="tripOrder" items="${tripOrders}">
            <tr>
                <td>${tripOrder.departure.address}</td>
                <td>${tripOrder.destination.address}</td>
                <td>${tripOrder.category}</td>
                <td>${tripOrder.capacity}</td>
                <td>
                    <a style="color: black;" href="${pageContext.request.contextPath}/profileView?login=${tripOrder.user.login}">${tripOrder.user.lastname} ${tripOrder.user.firstname}</p>
                </td>
                <td>
                    <a style="color: black;" href="${pageContext.request.contextPath}/carView?id=${tripOrder.car.id}">${tripOrder.car.carName}, ${tripOrder.car.licensePlate}</p>
                </td>
                <td>${tripOrder.price}</td>
                <td>
                    <fmt:parseDate value="${tripOrder.timestamp}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
                    <fmt:formatDate pattern="HH:mm dd MMM yyyy" value="${parsedDateTime}" />
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <div style="width: 100%; text-align: center;">
        <ul class="complex_list">
            <c:if test="${currentPage != 1}">
                <li><a href="${pageContext.request.contextPath}/orderStatistics?currentPage=${currentPage - 1}"> < </a></li>
            </c:if>
                <c:forEach begin="1" end="${pagesQuantity}" var="i">
                    <c:choose>
                        <c:when test="${currentPage eq i}">
                            ${i}
                        </c:when>
                        <c:otherwise>
                            <li><a href="${pageContext.request.contextPath}/orderStatistics?currentPage=${i}">${i}</a></li>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            <c:if test="${currentPage lt pagesQuantity}">
                <li><a href="${pageContext.request.contextPath}/orderStatistics?currentPage=${currentPage + 1}"> > </a></li>
            </c:if>
        </ul>
    </div>
</div>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
</body>
</html>