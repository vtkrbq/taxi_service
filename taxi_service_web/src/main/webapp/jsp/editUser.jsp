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
<div class="page-wrapper">
    <div class="left-panel-wrapper">
        <div class="left-panel">
            <form action="editUser" method="post">
                <table style="font-size: 24px; font-family: Sans;">
                    <caption>
                        <div style="padding-left: 0px; display: inline-block;">
                            <b style="font-size: 32px; color: black;">${currentUser.login}</b>
                            <button type="submit" class="btn btn-secondary" style="background-color: black; width: 100px; margin-left: 40px;"><fmt:message key="save.button" /></button>
                            <a href="${pageContext.request.contextPath}/profile" class="btn btn-secondary" style="width: 100px; margin-left: 40px;"><fmt:message key="cancel.button" /></a>
                        </div>
                    </caption>
                    <tr>
                        <td style="width: 150px;"><fmt:message key="login.label" />:</td>
                        <td><input type="text" name="login" class="form-control" value="${currentUser.login}" disabled></td>
                    </tr>
                    <tr>
                        <td style="width: 150px;"><fmt:message key="password.label" />:</td>
                        <td>
                            <input type="password" name="password" class="form-control" value="********" disabled>
                            <a href="${pageContext.request.contextPath}/changePassword" style="font-size: 12px; width: 100px;"><fmt:message key="a.change.pass" /></a>
                        </td>

                    </tr>
                    <tr>
                        <td><fmt:message key="name.label" />:</td>
                        <td><input type="text" name="firstname" class="form-control" value="${currentUser.firstname}"></td>
                    </tr>
                    <tr>
                        <td><fmt:message key="lastname.label" />:</td>
                        <td><input type="text" name="lastname" class="form-control" value="${currentUser.lastname}"></td>
                    </tr>
                    <tr>
                        <td><fmt:message key="phone.label" />:</td>
                        <td><input type="text" name="phone" class="form-control" value="${currentUser.phone}"></td>
                    </tr>
                    <tr>
                        <td><fmt:message key="email.label" />:</td>
                        <td><input type="text" name="email" class="form-control" value="${currentUser.email}"></td>
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
<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
</body>
</html>