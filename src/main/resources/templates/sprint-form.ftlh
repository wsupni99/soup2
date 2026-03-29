<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title><#if sprint.getId()??>Редактирование спринта<#else>Новый спринт</#if></title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 700px;
            margin: 40px auto;
            padding: 20px;
        }
        form {
            display: flex;
            flex-direction: column;
            gap: 15px;
        }
        label {
            font-weight: bold;
            margin-top: 8px;
        }
        input, select {
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
            font-size: 16px;
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

<h1>
    <#if sprint.getId()??>
        Редактирование спринта
    <#else>
        Новый спринт
    </#if>
</h1>
<#if error??>
    <div class="error">${error}</div>
</#if>

<form method="post"
      action="<#if sprint.getId()??>/sprints/${sprint.getId()}/update<#else>/sprints</#if>">

    <#if sprint.getId()??>
        <input type="hidden" name="id" value="${sprint.getId()}">
    </#if>

    <label>Название спринта *</label>
    <input type="text" name="name" value="${sprint.getName()!''}" required>

    <label>Проект *</label>
    <select name="projectId" required>
        <option value="">-- Выберите проект --</option>
        <#list projects as project>
            <option value="${project.id}"
                    <#if sprint.getProjectId()?? && sprint.getProjectId() == project.id>selected</#if>>
                ${project.name!''}
            </option>
        </#list>
    </select>

    <label>Дата начала *</label>
    <input type="date" name="startDate" value="${(startDateStr)!''}" required>

    <label>Дата окончания *</label>
    <input type="date" name="endDate" value="${(endDateStr)!''}" required>

    <button type="submit">
        <#if sprint.getId()??>
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