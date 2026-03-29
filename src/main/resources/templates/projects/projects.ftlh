<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Проекты</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<h1>Проекты</h1>

<#if error??>
    <div class="error">${error}</div>
</#if>

<a href="/projects/new" class="btn btn-primary">+ Создать проект</a>
<a href="/tasks" class="btn">← К задачам</a>

<table>
    <tr>
        <th>ID</th>
        <th>Название</th>
        <th>Описание</th>
        <th>Статус</th>
        <th>Менеджер</th>
        <th>Действия</th>
    </tr>
    <#list projects as project>
        <tr>
            <td>${project.getId()!''}</td>
            <td>${project.getName()!''}</td>
            <td>${project.getDescription()!''}</td>
            <td>${project.getStatus()!''}</td>
            <td>${project.getManagerName()!''}</td>
            <td>
                <a href="/projects/${project.getId()}/edit" class="btn btn-edit">Редактировать</a>
                <a href="/projects/${project.getId()}/delete"
                   class="btn btn-danger"
                   onclick="return confirm('Вы действительно хотите удалить этот проект?')">Удалить</a>
            </td>
        </tr>
    </#list>
</table>
</body>
</html>