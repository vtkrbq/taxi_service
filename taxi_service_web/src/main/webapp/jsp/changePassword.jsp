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
    <link href="https://fonts.googleapis.com/css2?family=Exo+2&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="http://cdn.leafletjs.com/leaflet-0.7.3/leaflet.css" />
    <script src="http://cdn.leafletjs.com/leaflet-0.7.3/leaflet.js"></script>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
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
				<li class="active"><a href="${pageContext.request.contextPath}/ordering">Home</a></li>
				<li><a href="#">Services</a></li>
				<li><a href="#">Contact</a></li>
			</ul>
		</nav>
</header>
<div class="page-wrapper">
    <div class="left-panel-wrapper">
        <div class="left-panel">
            <form action="changePassword" method="post">
                <table style="font-size: 24px; font-family: Sans;">
                    <caption>
                        <div style="padding-left: 0px; display: inline-block;">
                            <button type="submit" class="btn btn-secondary" style="background-color: black; width: 100px; margin-left: 40px;">Change</button>
                            <a href="${pageContext.request.contextPath}/profile" class="btn btn-secondary" style="width: 100px; margin-left: 40px;">Cancel</a>
                        </div>
                    </caption>
                    <tr>
                        <td>Old password:</td>
                        <td>
                            <input type="password" name="oldPassword" class="form-control">
                        </td>
                    </tr>
                    <tr>
                        <td>New password:</td>
                        <td>
                            <input type="password" name="newPassword" class="form-control">
                        </td>
                    </tr>
                    <tr>
                        <td>Confirm new password:</td>
                        <td>
                            <input type="password" name="newConfirmPassword" class="form-control">
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td style="font-size: 12px;">
                            <c:if test="${errors != null && not empty errors}">
                                <div style="color: red">
                                    <c:forEach items="${errors}" var="error" >
                                        <div><c:out value="${error}"/></div>
                                    </c:forEach>
                                </div>
                            </c:if>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
</div>

<div class="content-wrapper">
    <div class="content">
    </div>
</div>
</body>
</html>