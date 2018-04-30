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
});

function updateTable() {
    $.get(ajaxUrl, function (data) {
        datatableApi.clear().rows.add(data).draw();
    });
}

function changeUserState(state, id) {
    var selector = "#" + id;
    $.ajax({
        type: "PUT",
        url: ajaxUrl + id + '/' + state,
        success: function () {
            if (state) {
                $(selector).removeClass(disabledUserClass);
            } else {
                $(selector).addClass(disabledUserClass);
            }
        }
    });
}