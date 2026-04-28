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
    <#if myRequests?has_content>
    <#else>
        <p>Активных заявок нет</p>
    </#if>

    <h2>Доступные проекты</h2>
    <div class="project-grid">
        <#list availableProjects as project>
            <div class="project-card">
                <h3>${project.name}</h3>
                <button onclick="sendRequest(${project.id})" class="btn btn-primary">Подать заявку</button>
            </div>
        </#list>
    </div>
</div>

<script>
    function sendRequest(projectId) {
        fetch('/project-requests/request?projectId=' + projectId, {
            method: 'POST',
            headers: {'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').content}
        }).then(() => location.reload());
    }
</script>
</html>