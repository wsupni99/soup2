<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Создание пользователя</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>

<h1>Создание пользователя</h1>

<form class="container-form" method="post" action="/admin/users/create">
    <label>Email <span class="required">*</span></label>
    <input type="email" name="email" value="${createDto.getEmail()!''}" required>

    <label>Пароль <span class="required">*</span></label>
    <input type="password" name="password" value="${createDto.getPassword()!''}" required>

    <label>Имя <span class="required">*</span></label>
    <input type="text" name="name" value="${createDto.getName()!''}" required>

    <label>Контактная информация</label>
    <input type="text" name="contactInfo" value="${createDto.getContactInfo()!''}">

    <label>Роль <span class="required">*</span></label>
    <select name="roleName" required>
        <option value="">-- Выберите роль --</option>
        <#list roles as role>
            <option value="${role.roleName!''}"
                    ${(createDto.getRoleName()?? && createDto.getRoleName() == role.roleName)?string('selected','')}>
                ${role.roleName!''}
            </option>
        </#list>
    </select>

    <button type="submit">Создать</button>
</form>

<br>
<a href="/admin/users">← Назад к списку</a>

</body>
</html>