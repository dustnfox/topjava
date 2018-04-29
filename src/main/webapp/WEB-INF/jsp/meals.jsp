<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<script type="text/javascript" src="resources/js/datatablesUtil.js" defer></script>
<script type="text/javascript" src="resources/js/mealDatatables.js" defer></script>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>

<div class="jumbotron">
    <div class="container">
        <h3><spring:message code="meal.title"/></h3>
        <br/>
        <div class="dataTables_filter">
            <form id="filterForm">
                <div class="form-group row">
                    <div class="col-sm-10 row">
                        <label for="startDate" class="col-form-label col-sm-2"><spring:message
                                code="meal.startDate"/></label>
                        <div class="col-sm-2">
                            <input class="form-control datePicker" type="text" name="startDate" id="startDate">
                        </div>

                        <label for="startTime" class="col-form-label col-sm-2"><spring:message
                                code="meal.startTime"/></label>
                        <div class="col-sm-2">
                            <input class="form-control timePicker" type="text" name="startTime" id="startTime">
                        </div>
                    </div>

                    <div class="col-sm-10 row">
                        <label for="endDate" class="col-form-label col-sm-2"><spring:message
                                code="meal.endDate"/></label>
                        <div class="col-sm-2">
                            <input class="form-control datePicker" type="text" name="endDate" id="endDate">
                        </div>

                        <label for="endTime" class="col-form-label col-sm-2"><spring:message
                                code="meal.endTime"/></label>
                        <div class="col-sm-2">
                            <input class="form-control timePicker" type="text" name="endTime" id="endTime">
                        </div>
                    </div>
                </div>
            </form>
            <button class="btn btn-danger" type="button" onclick="clearFilter()">
                <span class="fa fa-remove"></span>
                <spring:message code="common.cancel"/>
            </button>
            <button class="btn btn-primary" type="button" onclick="updateTable()">
                <span class="fa fa-filter"></span>
                <spring:message code="meal.filter"/>
            </button>
        </div>
        <hr>
        <button class="btn btn-primary" onclick="add()">
            <span class="fa fa-plus"></span>
            <spring:message code="common.add"/>
        </button>
        <table class="table table-striped" border="1" cellpadding="8" cellspacing="0" id="datatable">
            <thead>
            <tr>
                <th><spring:message code="meal.dateTime"/></th>
                <th><spring:message code="meal.description"/></th>
                <th><spring:message code="meal.calories"/></th>
                <th></th>
                <th></th>
            </tr>
            </thead>
            <c:forEach items="${meals}" var="meal">
                <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.to.MealWithExceed"/>
                <tr data-mealExceed="${meal.exceed}" id="${meal.id}">
                    <td>
                            <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                            <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                            <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                            ${fn:formatDateTime(meal.dateTime)}
                    </td>
                    <td>${meal.description}</td>
                    <td>${meal.calories}</td>
                    <td><a class="edit"><span class="fa fa-pencil"></span></a></td>
                    <td><a class="delete"><span class="fa fa-remove"></span></a></td>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>

<div class="modal fade" tabindex="-1" id="editRow">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><spring:message code="meal.add"/></h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <form id="detailsForm">
                    <input type="hidden" id="id" name="id">

                    <div class="form-group">
                        <label for="dateTime" class="col-form-label"><spring:message code="meal.dateTime"/></label>
                        <input type="text" class="form-control" id="dateTime" name="datetime"
                               placeholder="<spring:message code="meal.dateTime"/>">
                    </div>

                    <div class="form-group">
                        <label for="description" class="col-form-label"><spring:message
                                code="meal.description"/></label>
                        <input type="text" class="form-control" id="description" name="description"
                               placeholder="<spring:message code="meal.description"/>">
                    </div>

                    <div class="form-group">
                        <label for="calories" class="col-form-label"><spring:message code="meal.calories"/></label>
                        <input type="number" class="form-control" id="calories" name="calories"
                               placeholder="<spring:message code="meal.calories"/>">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">
                    <span class="fa fa-close" aria-hidden="true"></span>
                    <spring:message code="common.cancel"/>
                </button>
                <button type="button" class="btn btn-primary" onclick="save()">
                    <span class="fa fa-check" aria-hidden="true"></span>
                    <spring:message code="common.save"/>
                </button>
            </div>
        </div>
    </div>
</div>

<jsp:include page="fragments/footer.jsp"/>
</body>
</html>