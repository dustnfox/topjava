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
<div class="meals-list-div">

    <h3><a href="index.html">Home</a></h3>
<h2 align="center">Meals list</h2>
    <table class="meals-table">
        <tr>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td><a href="meals?action=create"><img src="img/add.png"/></a></td>
        </tr>
        <tr>
            <th>Time</th>
            <th>Description</th>
            <th>Calories</th>
            <th colspan="2">Actions</th>
        </tr>
        <c:forEach var="meal" items="${meals}">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealWithExceed"/>
            <c:set var="rowClass" value="${meal.exceed ? 'exceed' : 'non-exceed'}"/>
            <tr class="${rowClass}">
                <td>${f:getLocalDate(meal.dateTime)}&nbsp${f:getLocalTime(meal.dateTime)}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=edit&id=${meal.id}"><img src="img/edit.png"/></a></td>
                <td><a href="meals?action=delete&id=${meal.id}"><img src="img/remove.png"/></a></td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>
