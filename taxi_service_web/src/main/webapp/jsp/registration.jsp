<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="css/style.css" />
    <link href="https://fonts.googleapis.com/css2?family=Exo+2&display=swap" rel="stylesheet">
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
    <script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
    <title>Welcome</title>
</head>
<body>
<h2>Registration</h2>
<div class="sidenav">
    <div class="login-main-text">
        <p>Login or register from here to access.</p>
        <img src="img/main.png" style="width: 600px; height: 600px;" alt="Taxi service">
    </div>
</div>
<div class="main">
    <div class="col-md-6 col-sm-12">
        <div class="register-form">
            <form action="registration" method="post">
                <div class="form-group">
                    <label>Login</label>
                    <input type="text" name="login" class="form-control" placeholder="Login" value="${user.login}">
                </div>
                <div class="form-group">
                    <label>Password</label>
                    <input type="password" name="password" class="form-control" placeholder="Password" >
                </div>
                <div class="form-group">
                    <label>Confirm Password</label>
                    <input type="password" name="confirmPassword" class="form-control" placeholder="Confirm password">
                </div>
                <div class="form-group">
                    <label>Name</label>
                    <input type="text" name="firstname" class="form-control" placeholder="Name" value="${user.firstname}">
                </div>
                <div class="form-group">
                    <label>Lastname</label>
                    <input type="text" name="lastname" class="form-control" placeholder="Lastname" value="${user.lastname}">
                </div>
                <div class="form-group">
                    <label>Phone</label>
                    <input type="text" name="phone" class="form-control" placeholder="Phone" value="${user.phone}">
                </div>
                <div class="form-group">
                    <label>Email</label>
                    <input type="text" name="email" class="form-control" placeholder="Email" value="${user.email}">
                </div>
                <div class="form-group">
                    <input type="checkbox" name="registerAsDriver">
                    <label>Register as driver</label>
                </div>
                <button type="submit" class="btn btn-black">Register</button>
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
