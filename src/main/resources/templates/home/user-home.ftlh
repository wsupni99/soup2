<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Главная</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<#include "../header.ftlh">

<div class="main-content">
    <h1>Главная страница</h1>

    <h2>Мои проекты</h2>
    <div class="project-grid">
        <#list myProjects as project>
            <div class="project-card">
                <h3>${project.name}</h3>
                <p>Менеджер: ${project.managerName!''}</p>
                <a href="/dashboard/${project.id}" class="btn btn-primary">Перейти к доске</a>
            </div>
        </#list>
    </div>

    <h2>Мои заявки</h2>
    <div class="request-grid">
        <#if myRequests?? && myRequests?size gt 0>
            <#list myRequests as request>
                <div class="project-card">
                    <h3>Заявка на проект: ${request.project.name}</h3>
                    <p>Статус: <strong>Ожидает подтверждения</strong></p>
                    <p>Дата подачи: ${request.createdAt!''}</p>
                </div>
            </#list>
        <#else>
            <p class="empty">Активных заявок нет</p>
        </#if>
    </div>

    <h2>Доступные проекты</h2>
    <div class="project-grid">
        <#list availableProjects as project>
            <div class="project-card">
                <h3>${project.name}</h3>
                <p>Менеджер: ${project.managerName!''}</p>
                <button onclick="sendRequest(${project.id})" class="btn btn-primary">Подать заявку</button>
            </div>
        </#list>
    </div>
</div>

<script>
    function sendRequest(projectId) {
        const csrfMeta = document.querySelector('meta[name="_csrf"]');
        const csrfToken = csrfMeta ? csrfMeta.content : '';

        if (!csrfToken) {
            console.error("CSRF token not found!");
            alert("Ошибка: CSRF токен не найден. Обновите страницу.");
            return;
        }

        fetch('/project-requests/request?projectId=' + projectId, {
            method: 'POST',
            headers: {
                'X-CSRF-TOKEN': csrfToken,
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.ok) {
                    alert("Заявка успешно отправлена!");
                    location.reload();
                } else {
                    return response.text().then(text => {
                        alert("Ошибка: " + text);
                    });
                }
            })
            .catch(error => {
                console.error("Fetch error:", error);
                alert("Произошла ошибка при отправке заявки");
            });
    }
</script>
</html>