<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Спринты</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<#include "../header.ftlh">
<div class="main-content">
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
                <td>${sprint.id!''}</td>
                <td>${sprint.name!''}</td>
                <td>${sprint.projectName!''}</td>
                <td>${sprint.startDate!''}</td>
                <td>${sprint.endDate!''}</td>
                <td>${sprint.taskCount!0}</td>
                <td>
                    <a href="/sprints/${sprint.id}/edit" class="btn btn-edit">Редактировать</a>
                    <a href="/sprints/${sprint.id}/delete"
                       class="btn btn-danger"
                       onclick="return confirm('Вы действительно хотите удалить этот спринт?')">Удалить</a>
                </td>
            </tr>
        </#list>
    </table>
    </body>
</div>
</html>