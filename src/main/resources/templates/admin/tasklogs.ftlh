<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Лог задач</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>

<#assign pmin = (currentPage - 4 > 0)?then(currentPage - 4, 0)>

<h1>Лог задач</h1>

<div class="stats">
    Всего записей: ${totalElements} | Страница ${currentPage + 1} из ${totalPages}
</div>

<div class="filter-form">
    <form method="get" action="/admin/tasklogs" id="filterForm">
        <label>Пользователь:
            <select name="userId">
                <option value="">Все пользователи</option>
                <#list users as user>
                    <option value="${user.id?c}"
                            <#if selectedUserId?? && selectedUserId == user.id>selected</#if>>
                        ${user.name!''} (${user.email!''})
                    </option>
                </#list>
            </select>
        </label>

        <label>Записей на странице:
            <select name="size">
                <option value="25">25</option>
                <option value="50" selected>50</option>
                <option value="100">100</option>
            </select>
        </label>

        <button type="submit" class="btn btn-primary">Применить</button>
        <a href="/admin/tasklogs" class="btn">Сбросить</a>
    </form>
</div>

<#if logs?? && (logs?size > 0)>
    <table>
        <tr>
            <th>ID</th>
            <th>Действие</th>
            <th>Дата/время</th>
            <th>Пользователь</th>
            <th>Задача ID</th>
        </tr>
        <#list logs as log>
            <tr>
                <td>${log.getId()!''}</td>
                <td>${log.getAction()!''}</td>
                <td>${log.getChangedAt()!''}</td>
                <td>${log.getUserName()!''} (ID: ${log.getUserId()!''})</td>
                <td>${log.getTaskId()!''}</td>
            </tr>
        </#list>
    </table>

    <#assign pmin = (currentPage - 4 > 0)?then(currentPage - 4, 0)>
    <#assign pmax = (currentPage + 5 < totalPages)?then(currentPage + 5, totalPages - 1)>

    <div class="pagination">
        <#if currentPage gt 0>
            <a href="/admin/tasklogs?page=${currentPage - 1}<#if selectedUserId??>&userId=${selectedUserId?c}</#if>&size=50"
               class="page-link">« Предыдущая</a>
        </#if>

        <#list pmin..pmax as i>
            <a href="/admin/tasklogs?page=${i}<#if selectedUserId??>&userId=${selectedUserId?c}</#if>&size=50"
               class="page-link <#if i == currentPage>active</#if>">${i + 1}</a>
        </#list>

        <#if currentPage lt totalPages - 1>
            <a href="/admin/tasklogs?page=${currentPage + 1}<#if selectedUserId??>&userId=${selectedUserId?c}</#if>&size=50"
               class="page-link">Следующая »</a>
        </#if>
    </div>
<#else>
    <div class="empty">Записи не найдены</div>
</#if>

<br><br>
<a href="/admin/users" class="btn btn-primary">К пользователям</a>
<a href="/admin/users" class="btn btn-primary">Админка</a>

</body>
</html>