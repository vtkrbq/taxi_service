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
<div class="sidenav">
    <div class="login-main-text">
        <p>Login or register from here to access.</p>
        <img src="img/main.png" style="width: 600px; height: 600px;" alt="Taxi service">
    </div>
</div>
<div class="main">
    <div class="col-md-6 col-sm-12">
        <div class="register-form">
            <form action="${pageContext.request.contextPath}/carRegistration" method="post">
                <div class="form-group">
                    <label>Car</label>
                    <input type="text" name="carName" class="form-control" placeholder="Car" value="${car.carName}">
                </div>
                <div class="form-group">
                    <label>Car category</label>
                    <div class="select">
                        <select name="carCategory" value="${car.carCategory}">
                            <option>Economy</option>
                            <option>Comfort</option>
                            <option>Business</option>
                        </select>
                        <span class="focus"></span>
                    </div>
                </div>
                <div class="form-group">
                    <label>Car capacity</label>
                    <input type="text" name="carCapacity" class="form-control" placeholder="Car capacity" value="${car.carCapacity}">
                </div>
                <div class="form-group">
                    <label>License plate</label>
                    <input type="text" name="licensePlate" class="form-control" placeholder="Name" value="${car.licensePlate}">
                </div>
                <button type="submit" class="btn btn-black">Register car</button>
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
