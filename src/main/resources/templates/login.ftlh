<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Вход в систему</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f0f2f5; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }
        .container { background: white; padding: 40px; border-radius: 10px; box-shadow: 0 4px 15px rgba(0,0,0,0.1); width: 380px; }
        input { width: 100%; padding: 12px; margin: 10px 0; border: 1px solid #ccc; border-radius: 5px; }
        button { width: 100%; padding: 12px; background: #4e73df; color: white; border: none; border-radius: 5px; font-size: 16px; cursor: pointer; }
        button:hover { background: #2e59d9; }
        .error { color: red; text-align: center; }
    </style>
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

    <form action="/login" method="post">
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