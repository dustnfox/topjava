var ajaxUrl = "ajax/meals/";
var datatableApi;

$(function () {
    datatableApi = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "dateTime"
            },
            {
                "data": "description"
            },
            {
                "data": "calories"
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
                "desc"
            ]
        ]
    });
    makeEditable();

    $("#dateTime").datetimepicker({
        locale: false
    });

    $(".datePicker").datetimepicker({
        locale: false,
        timepicker: false,
        format: "Y-m-d"
    });

    $(".timePicker").datetimepicker({
        locale: false,
        datepicker: false,
        format: "H:i"
    });
});

function clearFilter() {
    $("#filterForm").find("input:text").val("");
    updateTable();
}

function getUpdateParams() {
    return $("#filterForm").serialize();
}