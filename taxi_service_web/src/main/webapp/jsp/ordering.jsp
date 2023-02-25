<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <title>Taxi Service</title>
    <link rel="stylesheet" type="text/css" href="css/header.css" />
    <link rel="stylesheet" type="text/css" href="css/ordering.css" />
    <link rel="stylesheet" type="text/css" href="css/style.css" />
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Exo+2:wght@300&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="resources/leaflet/leaflet.css" />
    <link rel="stylesheet" href="resources/plugins/leaflet-routing-machine/dist/leaflet-routing-machine.css" />
    <link rel="stylesheet" href="resources/plugins/leaflet.contextmenu.css" />
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <style>
        .control {
            z-index: 99999998;
            padding: 25px;
            background-color: white;
            position: absolute;

        }
    </style>
</head>
<body>
<script src="resources/leaflet/leaflet.js"></script>
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
            <li class="active"><a href="${pageContext.request.contextPath}/ordering">Home</a></li>
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
<div style="position: relative;">
    <div id="control" class="control">
        <form action="${pageContext.request.contextPath}/ordering" method="post">
            <div>
               <label>
                   <b> Departure X: </b>
                   <input type="text" id="dx" class="form-control" style="width: 400px;" name="departureX" value="${tripOrder.departure.x}">
               </label>
            </div>
            <div>
               <label>
                   <b> Departure Y: </b>
                   <input type="text" id="dy" class="form-control" style="width: 400px;" name="departureY" value="${tripOrder.departure.y}">
               </label>
            </div>
            <div>
               <label>
                   <b> Departure Address: </b>
                   <input type="text" id="depart" class="form-control" style="width: 400px;" name="departureAddress" value="${tripOrder.departure.address}" onfocusout="getDepartureCoords()">
               </label>
            </div>
            <div>
               <label>
                   <b> Destination X: </b>
                   <input type="text" class="form-control" style="width: 400px;" name="departureX" value="${tripOrder.destination.x}">
               </label>
            </div>
            <div>
               <label>
                   <b> Destination Y: </b>
                   <input type="text" class="form-control" style="width: 400px;" name="departureY" value="${tripOrder.destination.y}">
               </label>
            </div>
            <div>
               <label>
                   <b>Destination Address:</b>
                   <input type="text" class="form-control" style="width: 400px;" name="destinationAddress" value="${tripOrder.destination.address}">
               </label>
            </div>
            <div><b>Category:</b></div>
               <label>
                   Economy
                   <input type="radio" class="form-control" name="category" value="ECONOMY" ${tripOrder.category.name() == 'ECONOMY'?'checked':''}>
               </label>
               <span>&nbsp;&nbsp;&nbsp;</span>
               <label>
                   Comfort
                   <input type="radio" class="form-control" name="category" value="COMFORT" ${tripOrder.category.name() == 'COMFORT'?'checked':''}>
               </label>
               <span>&nbsp;&nbsp;&nbsp;</span>
               <label>
                   Premium
                   <input type="radio" class="form-control" name="category" value="BUSINESS" ${tripOrder.category.name() == 'BUSINESS'?'checked':''}>
               </label>
            <div class="form-group">
               <label><b>Capacity:</b></label><input type="text" class="form-control" style="width: 400px;" name="capacity" value="${tripOrder.capacity}">
            </div>
            <button type="submit" class="btn btn-black">Order</button>
            <%--@elvariable id="errors" type="java.util.List<String>"--%>
            <c:if test="${errors != null && not empty errors}">
               <div style="color: red">
                   <c:forEach items="${errors}" var="error" >
                       <div><c:out value="${error}"/></div>
                   </c:forEach>
               </div>
            </c:if>
            <div class="form-group">
                <textarea id="textarea">
                  Текст
                </textarea>
            </div>
        </form>
    </div>
    <div id="map" class="map"></div>
</div>
<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="resources/plugins/leaflet-routing-machine/dist/leaflet-routing-machine.js"></script>
<script src="js/main.js"></script>
<script src="resources/plugins/leaflet-control-geocoder/dist/Control.Geocoder.js"></script>
<script src="resources/plugins/leaflet.contextmenu.js"></script>
</body>
</html>