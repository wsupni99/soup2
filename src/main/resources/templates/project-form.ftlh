<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title><#if project.id()??>Редактирование проекта<#else>Новый проект</#if></title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 700px; margin: 40px auto; padding: 20px; }
        form { display: flex; flex-direction: column; gap: 15px; }
        label { font-weight: bold; margin-top: 8px; }
        input, select, textarea {
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 6px;
            font-size: 16px;
        }
        button {
            padding: 12px;
            background: #4e73df;
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
        }
        .error {
            color: red;
            background: #ffe6e6;
            padding: 12px;
            border-radius: 6px;
            margin: 10px 0;
        }
    </style>
</head>
<body>

<h1><#if project.id()??>Редактирование проекта<#else>Новый проект</#if></h1>

<#if error??>
    <div class="error">${error}</div>
</#if>

<form method="post" action="<#if project.id()??>/projects/${project.id()}/update<#else>/projects</#if>">

    <input type="hidden" name="id" value="${project.id()!''}">

    <label>Название проекта *</label>
    <input type="text" name="name" value="${project.name()!''}" required>

    <label>Описание</label>
    <textarea name="description" rows="4">${project.description()!''}</textarea>

    <label>Дата начала *</label>
    <input type="date" name="startDate" value="${(startDateStr)!''}" required>

    <label>Дата окончания *</label>
    <input type="date" name="endDate" value="${(endDateStr)!''}" required>

    <label>Статус</label>
    <select name="status">
        <option value="PLANNED"   ${(project.status()?? && project.status() == 'PLANNED')?string('selected','')}>Планируется</option>
        <option value="ACTIVE"    ${(project.status()?? && project.status() == 'ACTIVE')?string('selected','')}>Активный</option>
        <option value="COMPLETED" ${(project.status()?? && project.status() == 'COMPLETED')?string('selected','')}>Завершён</option>
        <option value="CANCELLED" ${(project.status()?? && project.status() == 'CANCELLED')?string('selected','')}>Отменён</option>
    </select>

    <label>Менеджер *</label>
    <select name="managerId" required>
        <option value="">Выберите менеджера</option>
        <#list managers as manager>
            <option value="${manager.id}"
                    ${(project.managerId()?? && project.managerId() == manager.id)?string('selected','')}>
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