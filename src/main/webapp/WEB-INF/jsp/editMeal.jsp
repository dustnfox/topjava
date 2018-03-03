<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="localDateFormatter" %>
<html>
<head>
    <title>Meal edit</title>
    <link rel="stylesheet" href="css/main.css">
</head>
<body>

<div class="meal-edit-div">
    <h2>Meal edit</h2>
    <form method="post" action="meals" enctype="application/x-www-form-urlencoded">
        <table class="meal-edit-table">
            <tr>
                <td>
                    <input type="hidden" name="id" value="${empty meal ? "" : meal.id}"/>
                </td>
            </tr>
            <tr>
                <td>Time and Date of the meal:</td>
                <td>
                    <input type="datetime-local" title="Date and time of the meal" name="datetime"
                           value="${empty meal ? "" : meal.dateTime.toString()}"/>
                </td>
            </tr>
            <tr>
                <td>Description:</td>
                <td>
                    <input type="text" name="description" value="${empty meal ? "" : meal.description}"
                           placeholder="Description"/>
                </td>
            </tr>
            <tr>
                <td>Calories:</td>
                <td>
                    <input type="number" name="calories" value="${empty meal ? "" : meal.calories}"
                           placeholder="Calories count"/>
                </td>
            </tr>
            <tr>
                <td colspan="2"><input type="submit" value="Send"/></td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
