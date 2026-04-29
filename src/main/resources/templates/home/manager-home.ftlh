<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Главная</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<#include "../header.ftlh">

<div class="main-content">
    <h1>Главная страница — Менеджер</h1>

    <!-- Быстрые действия -->
    <div class="quick-actions">
        <h2>Быстрые действия</h2>
        <a href="/projects/new" class="btn btn-primary">+ Создать проект</a>
        <a href="/projects" class="btn">Управление проектами</a>
    </div>

    <!-- Мои проекты -->
    <h2>Мои проекты</h2>
    <#if myProjects?? && myProjects?size gt 0>
        <div class="project-grid">
            <#list myProjects as project>
                <div class="project-card">
                    <h3>${project.name}</h3>
                    <p>Статус: ${project.status!''}</p>
                    <p>Период: ${project.startDate!''} — ${project.endDate!''}</p>
                    <a href="/dashboard/${project.id}" class="btn btn-primary">Перейти к доске</a>
                </div>
            </#list>
        </div>
    <#else>
        <p class="empty">У вас пока нет проектов</p>
    </#if>

    <!-- Заявки на вступление -->
    <h2>Заявки на вступление</h2>
    <#if pendingRequests?? && pendingRequests?size gt 0>
        <div class="request-grid">
            <#list pendingRequests as req>
                <div class="request-card">
                    <h3>${req.project.name}</h3>
                    <p><strong>От:</strong> ${req.user.name} (${req.user.email})</p>
                    <p>Дата подачи: ${req.createdAt}</p>

                    <div class="request-actions">
                        <button onclick="approveRequest(${req.id})" class="btn btn-primary">Одобрить</button>
                        <button onclick="rejectRequest(${req.id})" class="btn btn-danger">Отклонить</button>
                    </div>
                </div>
            </#list>
        </div>
    <#else>
        <p class="empty">Новых заявок на вступление нет</p>
    </#if>
</div>

<script>
    function approveRequest(requestId) {
        if (!confirm('Одобрить заявку?')) return;

        fetch('/project-requests/' + requestId + '/approve', {
            method: 'POST',
            headers: {
                'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').content
            }
        }).then(response => {
            if (response.ok) {
                location.reload();
            } else {
                alert('Ошибка при одобрении заявки');
            }
        });
    }

    function rejectRequest(requestId) {
        if (!confirm('Отклонить заявку?')) return;

        fetch('/project-requests/' + requestId + '/reject', {
            method: 'POST',
            headers: {
                'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').content
            }
        }).then(response => {
            if (response.ok) {
                location.reload();
            } else {
                alert('Ошибка при отклонении заявки');
            }
        });
    }
</script>
</html>