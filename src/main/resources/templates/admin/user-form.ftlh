<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>${isEdit?string('Редактирование', 'Создание')} пользователя</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<#include "../header.ftlh">

<div class="main-content container-form">

    <h1>${isEdit?string('Редактирование', 'Создание')} пользователя</h1>

    <form method="post"
          action="${isEdit?string('/admin/users/' + userDto.id + '/update', '/admin/users')}">

        <#if isEdit>
            <input type="hidden" name="id" value="${userDto.id!''}">
        </#if>
        <input type="hidden" name="_csrf" value="${_csrf.token}">

        <label>Email <span class="required">*</span></label>
        <input type="email" name="email" value="${userDto.email!''}" required>

        <#if !isEdit>
            <label>Пароль <span class="required">*</span></label>
            <input type="password" name="password" required>
        </#if>

        <label>Имя <span class="required">*</span></label>
        <input type="text" name="name" value="${userDto.name!''}" required>

        <label>Контактная информация</label>
        <input type="text" name="contactInfo" value="${userDto.contactInfo!''}">

        <label>Роль <span class="required">*</span></label>
        <select name="roleName" required>
            <option value="">-- Выберите роль --</option>
            <#list roles as role>
                <option value="${role.roleName!''}"
                        <#if userDto.roleName?? && userDto.roleName == role.roleName>selected</#if>>
                    ${role.roleName!''}
                </option>
            </#list>
        </select>

        <button type="submit">${isEdit?string('Сохранить', 'Создать')}</button>
    </form>

    <br>
    <a href="/admin/users">← Назад к списку</a>

</div>
</html>