<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title><#if sprint.id??>Редактирование спринта<#else>Новый спринт</#if></title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>

<h1>
    <#if sprint.id??>
        Редактирование спринта
    <#else>
        Новый спринт
    </#if>
</h1>
<#if error??>
    <div class="error">${error}</div>
</#if>

<form class="container-form" method="post"
      action="<#if sprint.id??>/sprints/${sprint.id}/update<#else>/sprints</#if>">

    <#if sprint.id??>
        <input type="hidden" name="id" value="${sprint.id}">
    </#if>
    <input type="hidden" name="_csrf" value="${_csrf.token}">
    <label>Название спринта *</label>
    <input type="text" name="name" value="${sprint.name!''}" required>

    <label>Проект *</label>
    <select name="projectId" required>
        <option value="">-- Выберите проект --</option>
        <#list projects as project>
            <option value="${project.id}"
                    <#if sprint.projectId?? && sprint.projectId == project.id>selected</#if>>
                ${project.name!''}
            </option>
        </#list>
    </select>

    <label>Дата начала *</label>
    <input type="date" name="startDate" value="${startDateStr!''}" required>

    <label>Дата окончания *</label>
    <input type="date" name="endDate" value="${endDateStr!''}" required>

    <button type="submit">
        <#if sprint.id??>
            Сохранить изменения
        <#else>
            Создать спринт
        </#if>
    </button>
</form>

<br>
<a href="/sprints">← Назад к списку спринтов</a>

</body>
</html>