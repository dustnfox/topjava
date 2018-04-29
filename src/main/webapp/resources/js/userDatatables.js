var ajaxUrl = "ajax/admin/users/";
var datatableApi;
var disabledUserClass = "table-danger";

// $(document).ready(function () {
$(function () {
    datatableApi = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "name"
            },
            {
                "data": "email"
            },
            {
                "data": "roles"
            },
            {
                "data": "enabled"
            },
            {
                "data": "registered"
            },
            {
                "defaultContent": "Edit",
                "orderable": false
            },
            {
                "defaultContent": "Delete",
                "orderable": false
            }
        ],
        "order": [
            [
                0,
                "asc"
            ]
        ]
    });
    makeEditable();

    $(".activeUser").click(function () {
        var id = getParentRowId($(this));
        changeUserState($(this).prop("checked"), id);
    })
});

function getUpdateParams() {
    return $("#filterForm").serialize();
}

function changeUserState(state, id) {
    var selector = "#" + id;
    $.ajax({
        type: "PUT",
        url: ajaxUrl + id + '/' + state
    });
    if (state) {
        $(selector).removeClass(disabledUserClass);
    } else {
        $(selector).addClass(disabledUserClass);
    }
}