<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Новый пользователь</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 600px; margin: 40px auto; }
        form { display: flex; flex-direction: column; gap: 15px; }
        label { font-weight: bold; }
        input, select { padding: 10px; border: 1px solid #ccc; border-radius: 6px; }
        button { padding: 12px; background: #4e73df; color: white; border: none; border-radius: 6px; cursor: pointer; }
        .error { color: red; background: #ffe6e6; padding: 12px; border-radius: 6px; }
    </style>
</head>
<body>
<h1>Создание нового пользователя</h1>

<#if error??>
    <div class="error">${error}</div>
</#if>

<form method="post" action="/admin/users">
    <label>Имя</label>
    <input type="text" name="name" value="${dto.name()!''}" required>

    <label>Email</label>
    <input type="email" name="email" value="${dto.email()!''}" required>

    <label>Пароль</label>
    <input type="password" name="password" required>

    <label>Роль</label>
    <select name="roleName" required>
        <option value="ROLE_DEVELOPER" <#if dto.roleName() == 'ROLE_DEVELOPER'>selected</#if>>Разработчик</option>
        <option value="ROLE_TESTER" <#if dto.roleName() == 'ROLE_TESTER'>selected</#if>>Тестировщик</option>
        <option value="ROLE_MANAGER" <#if dto.roleName() == 'ROLE_MANAGER'>selected</#if>>Менеджер</option>
    </select>

    <button type="submit">Создать пользователя</button>
</form>

<br>
<a href="/admin/users">← Назад к списку</a>
</body>
</html>