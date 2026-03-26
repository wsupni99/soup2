<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Проекты</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h1 { color: #333; }
        table { width: 100%; border-collapse: collapse; margin: 20px 0; }
        th, td { border: 1px solid #ccc; padding: 10px; text-align: left; }
        th { background: #f0f0f0; }
        .btn { padding: 8px 12px; text-decoration: none; border-radius: 4px; margin-right: 5px; }
        .btn-primary { background: #4e73df; color: white; }
        .btn-danger { background: #e74c3c; color: white; }
        .btn-edit { background: #f39c12; color: white; }
    </style>
</head>
<body>
<h1>Проекты</h1>
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
            <td>${project.id()!''}</td>
            <td>${project.name()!''}</td>
            <td>${project.description()!''}</td>
            <td>${project.status()!''}</td>
            <td>${project.managerName()!''}</td>
            <td>
                <a href="/projects/${project.id()}/edit">Редактировать</a>
                <!-- Если позже добавишь удаление -->
                <a href="/projects/${project.id()}/delete" onclick="return confirm('Вы уверены?')">Удалить</a>
            </td>
        </tr>
    </#list>
</table>
</body>
</html>