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
		    <a href="${pageContext.request.contextPath}/language?lang=ua"><img src="img/ua.png" alt="ua language" style="width: 25px; height:25px; border-radius: 25px;"></a>
            <img src="img/slash.png" alt="logo alt" style="width: 1px; height:25px;">
            <a href="${pageContext.request.contextPath}/language?lang=en"><img src="img/en.png" alt="en language" style="width: 25px; height:25px; border-radius: 25px;"></a>
		    <i class="fa fa-login"></i>
		    <div class="dropdown">
                <button class="mainmenubtn"><c:out value="${currentUser.firstname} ${currentUser.lastname}"/></button>
                    <div class="dropdown-child">
                        <a class="mainmenubtn" href="${pageContext.request.contextPath}/profile"><fmt:message key="ddm.profile.button" /></a>
                        <c:set var="driver" scope="request" value="false"/>
                        <c:forEach items="${sessionScope.currentUser.roles}" var="userRole" >
                            <c:if test="${userRole == 'DRIVER'}">
                                <c:set var="driver" scope="request" value="true"/>
                            </c:if>
                        </c:forEach>
                        <c:if test="${driver}">
                            <a class="mainmenubtn" href="${pageContext.request.contextPath}/carRegistration"><fmt:message key="ddm.car.reg.button" /></a>
                        </c:if>
                        <a class="mainmenubtn" href="${pageContext.request.contextPath}/logout"><fmt:message key="ddm.logout.button" /></a>
                    </div>
            </div>
		</div>
		<div class="mobile-menu-btn"><i class="fa fa-bars"></i> Меню</div>
		<nav class="main-menu top-menu">
        <ul>
            <li class="active"><a href="${pageContext.request.contextPath}/ordering"><fmt:message key="a.home" /></a></li>
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
            <li class="active"><a href="${pageContext.request.contextPath}/userStatistics"><fmt:message key="a.history" /></a></li>
            <c:if test="${admin}">
                <li class="active"><a href="${pageContext.request.contextPath}/orderStatistics"><fmt:message key="a.order.stat" /></a></li>
            </c:if>
            <c:if test="${driver}">
                <li class="active"><a href="${pageContext.request.contextPath}/driverStatistics"><fmt:message key="a.driver.history" /></a></li>
            </c:if>
        </ul>
		</nav>
</header>
<div style="padding-right: 10%; padding-left: 10%">
    <h2 style="padding-top: 10px; padding-bot: 10px"><fmt:message key="h2.label" /> ${currentUser.lastname} ${currentUser.firstname}<h2>
    <table class="table table-striped table-class" style="text-align: center;">
        <thead>
        <tr>
            <th><fmt:message key="depart.address.field" /></th>
            <th><fmt:message key="destin.address.field" /></th>
            <th><fmt:message key="category.field" /></th>
            <th><fmt:message key="capacity.field" /></th>
            <th><fmt:message key="car.field" /></th>
            <th><fmt:message key="price.field" />(₴)</th>
            <th><fmt:message key="created.field" /></th>
            <th><fmt:message key="completed.field" /></th>
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
                    <a style="color: black;" href="${pageContext.request.contextPath}/car?id=${tripOrder.car.id}">${tripOrder.car.carName}, ${tripOrder.car.licensePlate}</p>
                </td>
                <td>${tripOrder.price} <fmt:message key="money.label" />.</td>
                <td>
                    <fmt:parseDate value="${tripOrder.timestampCreated}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
                    <fmt:formatDate pattern="HH:mm dd MMM yyyy" value="${parsedDateTime}" />
                </td>
                <td>
                    <c:choose>
                      <c:when test="${tripOrder.timestampEnd == toCompare}">
                        <form method="post">
                            <input type="text" name="id" style="display: none;" value="${tripOrder.id}">
                            <input type="text" name="car_id" style="display: none;" value="${tripOrder.car.id}">
                            <button type="submit" style="btn btn-black; width: 100px; height: 30px; border-radius: 10px;"><fmt:message key="complete.button" /></button>
                        <form>
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
                <li><a href="${pageContext.request.contextPath}/driverStatistics?currentPage=${currentPage - 1}"> < </a></li>
            </c:if>
                <c:forEach begin="1" end="${pagesQuantity}" var="i">
                    <c:choose>
                        <c:when test="${currentPage eq i}">
                            <span style="text-decoration: underline; text-decoration-thickness: 1px;">${i}</span>
                        </c:when>
                        <c:otherwise>
                            <li><a href="${pageContext.request.contextPath}/driverStatistics?currentPage=${i}">${i}</a></li>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            <c:if test="${currentPage lt pagesQuantity}">
                <li><a href="${pageContext.request.contextPath}/driverStatistics?currentPage=${currentPage + 1}"> > </a></li>
            </c:if>
        </ul>
    </div>
</div>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
</body>
</html>