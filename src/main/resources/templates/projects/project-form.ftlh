<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title><#if project.getId()??>Редактирование проекта<#else>Новый проект</#if></title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>

<h1><#if project.getId()??>Редактирование проекта<#else>Новый проект</#if></h1>

<#if error??>
    <div class="error">${error}</div>
</#if>

<form class="container-form" method="post" action="<#if project.getId()??>/projects/${project.getId()}/update<#else>/projects</#if>">

    <input type="hidden" name="id" value="${project.getId()!''}">

    <label>Название проекта *</label>
    <input type="text" name="name" value="${project.getName()!''}" required>

    <label>Описание</label>
    <textarea name="description" rows="4">${project.getDescription()!''}</textarea>

    <label>Дата начала *</label>
    <input type="date" name="startDate" value="${(startDateStr)!''}" required>

    <label>Дата окончания *</label>
    <input type="date" name="endDate" value="${(endDateStr)!''}" required>

    <label>Статус</label>
    <select name="status">
        <option value="PLANNED"   ${(project.getStatus()?? && project.getStatus() == 'PLANNED')?string('selected','')}>Планируется</option>
        <option value="ACTIVE"    ${(project.getStatus()?? && project.getStatus() == 'ACTIVE')?string('selected','')}>Активный</option>
        <option value="COMPLETED" ${(project.getStatus()?? && project.getStatus() == 'COMPLETED')?string('selected','')}>Завершён</option>
        <option value="CANCELLED" ${(project.getStatus()?? && project.getStatus() == 'CANCELLED')?string('selected','')}>Отменён</option>
    </select>

    <label>Менеджер *</label>
    <select name="managerId" required>
        <option value="">Выберите менеджера</option>
        <#list managers as manager>
            <option value="${manager.id}"
                    ${(project.getManagerId()?? && project.getManagerId() == manager.id)?string('selected','')}>
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