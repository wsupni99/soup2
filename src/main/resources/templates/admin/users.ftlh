<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Управление пользователями</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h1 { color: #333; }
        table { width: 100%; border-collapse: collapse; margin: 20px 0; }
        th, td { border: 1px solid #ccc; padding: 10px; text-align: left; vertical-align: top; }
        th { background: #f0f0f0; }
        .btn {
            padding: 8px 12px;
            text-decoration: none;
            border-radius: 4px;
            margin-right: 5px;
            display: inline-block;
            border: none;
            cursor: pointer;
            font-size: 14px;
        }
        .btn-primary { background: #4e73df; color: white; }
        .btn-edit { background: #f39c12; color: white; }
        .btn-delete { background: #e74c3c; color: white; }
        .top-actions { margin-bottom: 20px; }
        .empty { color: #777; padding: 15px 0; }
    </style>
</head>
<body>
<h1>Управление пользователями</h1>

<div class="top-actions">
    <a href="/admin/users/create" class="btn btn-primary">+ Создать пользователя</a>
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

</body>
</html>