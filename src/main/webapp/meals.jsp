<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="localDateFormatter" %>
<html>
<head>
    <title>Meals</title>
    <link rel="stylesheet" href="css/main.css">
    <jsp:useBean id="meals" type="java.util.List<ru.javawebinar.topjava.model.MealWithExceed>" scope="request"/>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2 align="center">Meals list</h2>
    <table class="meals-table">
        <tr><th>Time</th><th>Description</th><th>Calories</th></tr>
        <c:forEach var="meal" items="${meals}">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealWithExceed"/>
            <c:set var="rowClass" value="${meal.exceed ? 'exceed' : 'non-exceed'}"/>
            <tr class="${rowClass}">
                <td>${f:getLocalDate(meal.dateTime)}&nbsp${f:getLocalTime(meal.dateTime)}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
