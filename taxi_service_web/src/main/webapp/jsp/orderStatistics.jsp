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
                        <c:set var="driver" scope="request" value="false"/>
                        <c:forEach items="${sessionScope.currentUser.roles}" var="userRole" >
                            <c:if test="${userRole == 'DRIVER'}">
                                <c:set var="driver" scope="request" value="true"/>
                            </c:if>
                        </c:forEach>
                        <c:if test="${driver}">
                            <a class="mainmenubtn" href="${pageContext.request.contextPath}/carRegistration">Car registration</a>
                        </c:if>
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
            <li class="active"><a href="${pageContext.request.contextPath}/userStatistics">Order history</a></li>
            <c:if test="${admin}">
                <li class="active"><a href="${pageContext.request.contextPath}/orderStatistics">Order statistics</a></li>
            </c:if>
            <c:if test="${driver}">
                <li class="active"><a href="${pageContext.request.contextPath}/driverStatistics">Driver statistics</a></li>
            </c:if>
        </ul>
		</nav>
</header>
<div style="padding-right: 10%; padding-left: 10%">
    <form action="orderStatistics" method="get">
        <div style="display: flex; justify-content: right;">
            <div style="display: flex; align-items: center; margin: 10px; margin-left: 20px; margin-right: 50px;">
                <label style="margin: 5px;">Records per page:</label>
                <select name="quantity" class="custom-select" style="margin: 5px; width: 100px;">
                  <option value="5" selected>5</option>
                  <option value="10">10</option>
                  <option value="15">15</option>
                  <option value="20">20</option>
                  <option value="25">25</option>
                </select>
                <label style="margin: 5px; text-align: right;">Sort:</label>
                <select name="sortType" class="custom-select" style="margin: 5px; width: 150px; padding-right: 0px;">
                  <option value="ASC">Ascending</option>
                  <option value="DESC" selected>Descending</option>
                </select>
                <label style="margin: 5px;">Sort by:</label>
                <select name="sortBy" class="custom-select" style="margin: 5px; width: 150px;">
                  <option value="DEPARTURE_ADDRESS">Departure address</option>
                  <option value="DESTINATION_ADDRESS">Destination address</option>
                  <option value="CATEGORY">Category</option>
                  <option value="CAPACITY">Capacity</option>
                  <option value="CLIENT_LAST_NAME">Client lastname</option>
                  <option value="CAR_NAME">Car</option>
                  <option value="PRICE">Price</option>
                  <option value="CREATED" selected>Created</option>
                </select>
                <label style="margin: 5px;">Filter by:</label>
                <select name="filterBy" class="custom-select" style="margin: 5px; width: 150px;">
                    <option value="" selected>Select...</option>
                    <option value="DEPARTURE_ADDRESS">Departure address</option>
                    <option value="DESTINATION_ADDRESS">Destination address</option>
                    <option value="CATEGORY">Category</option>
                    <option value="CAPACITY">Capacity</option>
                    <option value="CLIENT_LAST_NAME">Client name</option>
                    <option value="CAR_NAME">Car</option>
                    <option value="PRICE">Price</option>
                </select>
                <input type="text" name="filterKey" class="form-control" placeholder="Filter key" style="margin: 5px; width: 150px;">
                <input type="date" name="created" class="form-control" placeholder="Pick a date"  style="margin: 5px; width: 150px;">
                <button type="submit" class="btn btn-black" style="width: 100px; margin: 5px;">Filter</button>
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/orderStatistics" style="width: 100px; margin: 5px;">Reset</a>
            </div>
        </div>
    <form>
    <table class="table table-striped table-class" style="width: 100%">
        <thead>
        <tr>
            <th>Departure address</th>
            <th>Destination address</th>
            <th>Category</th>
            <th>Capacity</th>
            <th>Client name</th>
            <th>Car</th>
            <th>Price (₴)</th>
            <th>Created</th>
            <th>Completed</th>
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
                <td>${tripOrder.price} hrn.</td>
                <td>
                    <fmt:parseDate value="${tripOrder.timestampCreated}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
                    <fmt:formatDate pattern="HH:mm dd MMM yyyy" value="${parsedDateTime}" />
                </td>
                <td>
                    <c:choose>
                      <c:when test="${tripOrder.timestampEnd == toCompare}">
                        On route
                      </c:when>
                      <c:otherwise>
                        <fmt:parseDate value="${tripOrder.timestampEnd}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
                        <fmt:formatDate pattern="HH:mm dd MMM yyyy" value="${parsedDateTime}" />
                      </c:otherwise>
                    </c:choose>
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
                            <span style="text-decoration: underline; text-decoration-thickness: 1px;">${i}</span>
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