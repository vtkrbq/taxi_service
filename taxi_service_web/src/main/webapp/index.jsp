<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <title>Welcome</title>
    <link rel="stylesheet" type="text/css" href="css/style.css" />
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
    <script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
</head>
<body>
<div class="sidenav">
    <div class="login-main-text">
        <p>Login or register from here to access.</p>
        <img src="img/main.png" style="width: 600px; height: 600px;" alt="Taxi service">
    </div>
</div>
<div class="main">
    <div class="col-md-6 col-sm-12">
        <div class="login-form">
            <form action="login" method="post">
                <div class="form-group">
                    <label>Login</label>
                    <input type="text" name="login" class="form-control" placeholder="Login">
                </div>
                <div class="form-group">
                    <label>Password</label>
                    <input type="password" name="password" class="form-control" placeholder="Password">
                </div>
                    <button type="submit" class="btn btn-black">Login</button>
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/registration">Register</a>
                    <c:if test="${errors != null && not empty errors}">
                        <div style="color: red">
                            <c:forEach items="${errors}" var="error" >
                                <div><c:out value="${error}"/></div>
                            </c:forEach>
                        </div>
                    </c:if>
            </form>
        </div>
    </div>
</div>
</body>
</html>