<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="btn-group">
    <button class="btn btn-secondary btn-sm dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true"
            aria-expanded="false">
        ${pageContext.response.locale}
    </button>
    <div class="dropdown-menu">
        <a class="dropdown-item" href="${requestScope['javax.servlet.forward.request_uri']}?lang=en">en</a>
        <a class="dropdown-item" href="${requestScope['javax.servlet.forward.request_uri']}?lang=ru">ru</a>
    </div>
</div>