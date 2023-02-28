<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
<div class="page-wrapper">
    <div class="left-panel-wrapper">
        <div class="left-panel" style="display: flex;">
            <table style="font-size: 24px; font-family: Exo 2;">
                <caption>
                    <div style="padding-left: 0px; display: inline-block;">
                        <b style="font-size: 32px; color: black;">Car ID: ${carView.id}</b>
                        <a href="${pageContext.request.contextPath}/orderStatistics" class="btn btn-secondary" style="background-color: black; width: 100px; margin-left: 40px;">Back</a>
                    </div>
                </caption>
                <tr>
                    <td style="width: 150px;">Car:</td>
                    <td>${carView.carName}</td>
                </tr>
                <tr>
                    <td>License Plate:</td>
                    <td>${carView.licensePlate}</td>
                </tr>
                <tr>
                    <td>Category:</td>
                    <td>${carView.carCategory}</td>
                </tr>
                <tr>
                    <td>Capacity:</td>
                    <td>${carView.carCapacity}</td>
                </tr>
            </table>
            <table style="font-size: 24px; font-family: Exo 2;">
                <caption>
                    <div style="padding-left: 0px; display: inline-block;">
                        <b style="font-size: 32px; color: black;">Driver: ${carView.driver.login}</b>
                    </div>
                </caption>
                <tr>
                    <td style="width: 150px;">Name:</td>
                    <td>${carView.driver.firstname}</td>
                </tr>
                <tr>
                    <td>Lastname:</td>
                    <td>${carView.driver.lastname}</td>
                </tr>
                <tr>
                    <td>Phone:</td>
                    <td>${carView.driver.phone}</td>
                </tr>
                <tr>
                    <td>Email:</td>
                    <td>${carView.driver.email}</td>
                </tr>
            </table>
        </div>
</div>

<div class="content-wrapper">
    <div class="content">
    </div>
</div>
<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
</body>
</html>