<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Управление пользователями</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<#include "../header.ftlh">

<div class="main-content">
    <h1>Управление пользователями</h1>

    <div class="top-actions">
        <a href="/admin/users/new" class="btn btn-primary">+ Создать пользователя</a>
    </div>

    <#if users?? && users?size gt 0>
        <table>
            <tr>
                <th>ID</th>
                <th>Email</th>
                <th>Имя</th>
                <th>Контактная информация</th>
                <th>Роль</th>
                <th>Действия</th>
            </tr>
            <#list users as user>
                <tr>
                    <td>${user.id!''}</td>
                    <td>${user.email!''}</td>
                    <td>${user.name!''}</td>
                    <td>${user.contactInfo!''}</td>
                    <td>
                        <#if user.role??>
                            ${user.role.roleName!''}
                        <#else>
                            <span style="color: #999;">—</span>
                        </#if>
                    </td>
                    <td>
                        <a href="/admin/users/${user.id?c}/edit" class="btn btn-edit">Редактировать</a>
                    </td>
                </tr>
            </#list>
        </table>
    <#else>
        <div class="empty">Пользователи не найдены.</div>
    </#if>
</div>
</html>