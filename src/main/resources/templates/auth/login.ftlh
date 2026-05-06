<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Вход в систему</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body class="login-page">
<div class="container">
    <h2>Вход в систему</h2>

    <#if error??>
        <p class="error">Неверный email или пароль</p>
    </#if>
    <#if oauth_error??>
        <p class="error">Ошибка входа через Google</p>
    </#if>
    <#if message??>
        <p class="success-message">${message}</p>
    </#if>

    <form action="/login" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <input type="email" name="email" placeholder="Email" required autofocus>
        <input type="password" name="password" placeholder="Пароль" required autocomplete="current-password">

        <button type="submit">Войти</button>
    </form>

    <a href="/oauth2/authorization/google" class="btn btn-google">
        <span>Войти через Google</span>
    </a>

    <p>
        <a href="/register">Зарегистрироваться</a>
    </p>
</div>
</body>
</html>