<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title><#if project.id??>Редактирование проекта<#else>Новый проект</#if></title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>

<h1><#if project.id??>Редактирование проекта<#else>Новый проект</#if></h1>

<#if error??>
    <div class="error">${error}</div>
</#if>

<form class="container-form" method="post" action="<#if project.id??>/projects/${project.id}/update<#else>/projects</#if>">

    <#if project.id??>
        <input type="hidden" name="id" value="${project.id}">
    </#if>
    <input type="hidden" name="_csrf" value="${_csrf.token}">
    <label>Название проекта *</label>
    <input type="text" name="name" value="${project.name!''}" required>

    <label>Описание</label>
    <textarea name="description" rows="4">${project.description!''}</textarea>

    <label>Дата начала *</label>
    <input type="date" name="startDate" value="${startDateStr!''}" required>

    <label>Дата окончания *</label>
    <input type="date" name="endDate" value="${endDateStr!''}" required>

    <label>Статус</label>
    <select name="status">
        <option value="PLANNED"   <#if project.status?? && project.status == 'PLANNED'>selected</#if>>Планируется</option>
        <option value="ACTIVE"    <#if project.status?? && project.status == 'ACTIVE'>selected</#if>>Активный</option>
        <option value="COMPLETED" <#if project.status?? && project.status == 'COMPLETED'>selected</#if>>Завершён</option>
        <option value="CANCELLED" <#if project.status?? && project.status == 'CANCELLED'>selected</#if>>Отменён</option>
    </select>

    <label>Менеджер *</label>
    <select name="managerId" required>
        <option value="">Выберите менеджера</option>
        <#list managers as manager>
            <option value="${manager.id}"
                    <#if project.managerId?? && project.managerId == manager.id>selected</#if>>
                ${manager.name} (${manager.email})
            </option>
        </#list>
    </select>

    <button type="submit">Сохранить проект</button>
</form>

<br>
<a href="/projects">← Назад к списку проектов</a>

</body>
</html>