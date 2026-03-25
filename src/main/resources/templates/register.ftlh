<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Регистрация</title>
    <style>
        body { font-family: Arial, sans-serif; background: linear-gradient(135deg, #667eea, #764ba2); height: 100vh; display: flex; align-items: center; justify-content: center; margin: 0; }
        .container { background: white; padding: 40px 30px; border-radius: 12px; box-shadow: 0 10px 25px rgba(0,0,0,0.2); width: 100%; max-width: 420px; }
        h2 { text-align: center; margin-bottom: 30px; color: #333; }
        input, select { width: 100%; padding: 12px; margin: 10px 0; border: 1px solid #ddd; border-radius: 6px; box-sizing: border-box; }
        button { width: 100%; padding: 12px; background: #667eea; color: white; border: none; border-radius: 6px; font-size: 16px; cursor: pointer; margin-top: 15px; }
        button:hover { background: #5a6fd8; }
        a { display: block; text-align: center; margin-top: 20px; color: #667eea; }
    </style>
</head>
<body>
<div class="container">
    <h2>Регистрация</h2>

    <form action="/register" method="post">
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