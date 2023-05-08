<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8"%>
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
    <link rel="stylesheet" type="text/css" href="css/lang.css" />
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Exo+2:wght@300&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://api.visicom.ua/apps/visicom-autocomplete.min.css">
    <link rel="stylesheet" href="css/visicom-autocomplete.min.css">
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
<script src="https://api.visicom.ua/apps/visicom-autocomplete.min.js"></script>
<script src="resources/leaflet/leaflet.js"></script>
<script>
    function forward(control) {
      control.form.submit();
    }
</script>
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
            <li class="active"><a href="${pageContext.request.contextPath}/userStatistics"><fmt:message key="a.history" /></a>
            <c:if test="${admin}">
                <li class="active"><a href="${pageContext.request.contextPath}/orderStatistics"><fmt:message key="a.order.stat" /></a></li>
            </c:if>
            <c:if test="${driver}">
                <li class="active"><a href="${pageContext.request.contextPath}/driverStatistics"><fmt:message key="a.driver.history" /></a></li>
            </c:if>
        </ul>
		</nav>
</header>
<div style="position: relative;">
    <c:set var="noCars" scope="request" value="false"/>
    <div id="control" class="control">

        <form action="${pageContext.request.contextPath}/ordering" method="post">
            <div style="display: none;">
               <label>
                   <b> Distance: </b>
                   <input type="text" id="distance" class="form-control" style="width: 400px;" name="distance">
               </label>
            </div>
            <div style="display: none;">
               <label>
                   <b> Departure X: </b>
                   <input type="text" id="dx" class="form-control" style="width: 400px;" name="departureX" value="${tripOrder.departure.x}">
               </label>
            </div>
            <div style="display: none;">
               <label>
                   <b> Departure Y: </b>
                   <input type="text" id="dy" class="form-control" style="width: 400px;" name="departureY" value="${tripOrder.departure.y}">
               </label>
            </div>
            <div>
               <label>
                   <b> <fmt:message key="depart.address.field" />: </b>
                   <%
                        request.setCharacterEncoding("UTF-8");
                   %>
                   <div id="visicom-autocomplete-depart">
                       <a href="https://api.visicom.ua/" target="_blank" style="width: 400px; height: 40px;">© Visicom</a>
                  </div>
                  <input type="text" id="depart" class="form-control" style="display: none; width: 400px; height: 40px;" name="departureAddress" value="${tripOrder.departure.address}" onfocusout="getDepartureCoords()">
               </label>
            </div>
            <div style="display: none;">
               <label>
                   <b> Destination X: </b>
                   <input type="text" id="dsx" class="form-control" style="width: 400px;" name="destinationX" value="${tripOrder.destination.x}">
               </label>
            </div>
            <div style="display: none;">
               <label>
                   <b> Destination Y: </b>
                   <input type="text" id="dsy" class="form-control" style="width: 400px; height: 40px;" name="destinationY" value="${tripOrder.destination.y}">
               </label>
            </div>
            <div>
               <label>
                   <b><fmt:message key="destin.address.field" />:</b>
                   <%
                       request.setCharacterEncoding("UTF-8");
                   %>
                   <div id="visicom-autocomplete-destin">
                       <a href="https://api.visicom.ua/" target="_blank">© Visicom</a>
                   </div>
                   <input type="text" id="destin" class="form-control" style="display: none; width: 400px; height: 40px;" name="destinationAddress" value="${tripOrder.destination.address}" onfocusout="getDestinationCoords()">
               </label>
            </div>
            <div><b><fmt:message key="economy.label" />:</b></div>
               <label>
                   <fmt:message key="economy.label" />
                   <input type="radio" class="form-control" name="category" value="ECONOMY" ${tripOrder.category.name() == 'ECONOMY'?'checked':''}>
               </label>
               <span>&nbsp;&nbsp;&nbsp;</span>
               <label>
                   <fmt:message key="comfort.label" />
                   <input type="radio" class="form-control" name="category" value="COMFORT" ${tripOrder.category.name() == 'COMFORT'?'checked':''}>
               </label>
               <span>&nbsp;&nbsp;&nbsp;</span>
               <label>
                   <fmt:message key="premium.label" />
                   <input type="radio" class="form-control" name="category" value="BUSINESS" ${tripOrder.category.name() == 'BUSINESS'?'checked':''}>
               </label>
            <div class="form-group">
               <label><b><fmt:message key="capacity.field" />:</b></label><input type="text" class="form-control" style="width: 400px;" name="capacity" value="${tripOrder.capacity}">
            </div>
            <button type="submit" class="btn btn-black"><fmt:message key="order.button" /></button>
            <%--@elvariable id="errors" type="java.util.List<String>"--%>
            <c:if test="${errors != null && not empty errors}">
               <div style="color: red; padding-top: 10px;">
                   <c:forEach items="${errors}" var="error" >
                       <div><c:out value="${error}"/></div>
                       <c:set var="noCars" scope="request" value="true"/>
                   </c:forEach>
               </div>
            </c:if>
        </form>
    </div>
    <div id="map" class="map"></div>
</div>
<script>
    var dep = document.getElementById("depart");
    var des = document.getElementById("destin");
    ac = new visicomAutoComplete({
        selector: '#visicom-autocomplete-depart',
        apiKey : '9bc097f16c0606e0fb92a8e0ac130127',
        width: '400px',
        height: '40px',
        searchTextPrefix: 'м.Харків, ',
        onSuggestSelected: suggest =>
        {
            dep.value = suggest.html,
            getDepartureCoords();
        }
    });
    ac2 = new visicomAutoComplete({
        selector: '#visicom-autocomplete-destin',
        apiKey : '9bc097f16c0606e0fb92a8e0ac130127',
        width: '400px',
        height: '40px',
        searchTextPrefix: 'м.Харків, ',
        onSuggestSelected: suggest =>
        {
            des.value = suggest.html,
            getDestinationCoords();
        }
    });
</script>
<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="resources/plugins/leaflet-routing-machine/dist/leaflet-routing-machine.js"></script>
<script src="js/main.js"></script>
<script src="resources/plugins/leaflet-control-geocoder/dist/Control.Geocoder.js"></script>
<script src="resources/plugins/leaflet.contextmenu.js"></script>
</body>
</html>