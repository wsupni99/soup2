<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Регистрация</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<div class="container">
    <h2>Регистрация</h2>

    <form class="container-form" action="/register" method="post">
        <input type="text" name="name" placeholder="Ваше имя" required>
        <input type="email" name="email" placeholder="Email" required>
        <input type="password" name="password" placeholder="Пароль" required minlength="6">

        <select name="roleName" required>
            <option value="">Выберите роль</option>
            <option value="ROLE_DEVELOPER">Разработчик</option>
            <option value="ROLE_TESTER">Тестировщик</option>
            <option value="ROLE_MANAGER">Менеджер проекта</option>
        </select>

        <button type="submit">Зарегистрироваться</button>
    </form>

    <a href="/login">Уже есть аккаунт? Войти</a>
</div>
</body>
</html>