<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Спринты</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h1 { color: #333; }
        table { width: 100%; border-collapse: collapse; margin: 20px 0; }
        th, td { border: 1px solid #ccc; padding: 10px; text-align: left; }
        th { background: #f0f0f0; }
        .btn {
            padding: 8px 12px;
            text-decoration: none;
            border-radius: 4px;
            margin-right: 5px;
        }
        .btn-primary { background: #4e73df; color: white; }
        .btn-danger { background: #e74c3c; color: white; }
        .btn-edit { background: #f39c12; color: white; }
        .error {
            color: red;
            background: #ffe6e6;
            padding: 12px;
            border-radius: 6px;
            margin: 15px 0;
        }
    </style>
</head>
<body>
<h1>Спринты</h1>

<#if error??>
    <div class="error">${error}</div>
</#if>

<a href="/sprints/new" class="btn btn-primary">+ Новый спринт</a>
<a href="/projects" class="btn">← К проектам</a>

<table>
    <tr>
        <th>ID</th>
        <th>Название</th>
        <th>Проект</th>
        <th>Начало</th>
        <th>Окончание</th>
        <th>Задачи</th>
        <th>Действия</th>
    </tr>
    <#list sprints as sprint>
        <tr>
            <td>${sprint.id()!''}</td>
            <td>${sprint.name()!''}</td>
            <td>${sprint.projectName()!''}</td>
            <td>${sprint.startDate()!''}</td>
            <td>${sprint.endDate()!''}</td>
            <td>${sprint.taskCount()!0}</td>
            <td>
                <a href="/sprints/${sprint.id()}/edit" class="btn btn-edit">Редактировать</a>
                <a href="/sprints/${sprint.id()}/delete"
                   class="btn btn-danger"
                   onclick="return confirm('Вы действительно хотите удалить этот спринт?')">Удалить</a>
            </td>
        </tr>
    </#list>
</table>
</body>
</html>