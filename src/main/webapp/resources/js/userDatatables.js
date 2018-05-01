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
        updateTableWithData(data);
    });
}

function changeUserState(chkbox, id) {
    var selector = "#" + id;
    chkbox.checked = !chkbox.checked;

    $.ajax({
        type: "PUT",
        url: ajaxUrl + id + '/' + chkbox.checked,
        success: function () {
            chkbox.checked = !chkbox.checked;
            if (chkbox.checked) {
                $(selector).removeClass(disabledUserClass);
                successNoty("User enabled");
            } else {
                $(selector).addClass(disabledUserClass);
                successNoty("User disabled");
            }
        }
    });
}