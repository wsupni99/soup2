<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Вход в систему</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<div class="container">
    <h2 style="text-align:center;">Вход в систему</h2>

    <#if error??>
        <p class="error">Неверный email или пароль</p>
    </#if>
    <#if message??>
        <p style="color:green; text-align:center;">${message}</p>
    </#if>

    <form class="container-form" action="/login" method="post">
        <input type="email" name="email" placeholder="Email" required autofocus>
        <input type="password" name="password" placeholder="Пароль" required>

        <button type="submit">Войти</button>
    </form>

    <p style="text-align:center; margin-top:15px;">
        <a href="/register">Зарегистрироваться</a>
    </p>
</div>
</body>
</html>