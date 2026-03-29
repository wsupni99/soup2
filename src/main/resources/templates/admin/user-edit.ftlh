<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Редактирование пользователя</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 800px; margin: 40px auto; padding: 20px; }
        form { display: flex; flex-direction: column; gap: 15px; }
        label { font-weight: bold; margin-top: 8px; }
        input, select {
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 6px;
            font-size: 16px;
        }
        button {
            padding: 12px;
            background: #4e73df;
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 16px;
        }
        .required { color: red; }
    </style>
</head>
<body>

<h1>Редактирование пользователя</h1>

<form method="post" action="/admin/users/update">
    <input type="hidden" name="id" value="${updateDto.getId()!''}">

    <label>Email <span class="required">*</span></label>
    <input type="email" name="email" value="${updateDto.getEmail()!''}" required>

    <label>Имя <span class="required">*</span></label>
    <input type="text" name="name" value="${updateDto.getName()!''}" required>

    <label>Контактная информация</label>
    <input type="text" name="contactInfo" value="${updateDto.getContactInfo()!''}">

    <label>Роль <span class="required">*</span></label>
    <select name="roleName" required>
        <option value="">-- Выберите роль --</option>
        <#list roles as role>
            <option value="${role.roleName!''}"
                    ${(updateDto.getRoleName()?? && updateDto.getRoleName() == role.roleName)?string('selected','')}>
                ${role.roleName!''}
            </option>
        </#list>
    </select>

    <button type="submit">Сохранить</button>
</form>

<br>
<a href="/admin/users">← Назад к списку</a>

</body>
</html>