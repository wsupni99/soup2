<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <title>Дашборд — ${project.name!''}</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<#include "../header.ftlh">

<div class="main-content">
    <div class="dashboard-container">
        <div class="dashboard-header">
            <h1>Дашборд проекта: ${project.name}</h1>
        </div>

        <#if userProjects?has_content>
            <div class="project-switch">
                <#list userProjects as p>
                    <a href="/dashboard/${p.id?c}"
                       class="btn ${(p.id?long == project.id?long)?string('btn-primary', 'btn-outline')}">
                        ${p.name}
                    </a>
                </#list>
            </div>
        </#if>

        <div class="kanban-board" id="kanban">
            <#list ["TODO", "IN_PROGRESS", "DONE", "BLOCKED"] as status>
                <div class="kanban-column" data-status="${status}">
                    <div class="kanban-column-header">
                        <h3>${status?replace("_", " ")}</h3>
                        <span class="task-count" id="count-${status}">
                            <#assign count = 0>
                            <#list tasks as task>
                                <#if task.status == status>
                                    <#assign count = count + 1>
                                </#if>
                            </#list>
                            ${count}
                        </span>
                        <button onclick="showCreateTaskModal('${status}')"
                                class="btn btn-primary"
                                style="padding: 4px 10px; font-size: 0.9em;">
                            + Новая задача
                        </button>
                    </div>

                    <div class="kanban-tasks" id="tasks-${status}">
                        <#list tasks as task>
                            <#if task.status == status>
                                <div class="task-card"
                                     draggable="true"
                                     data-task-id="${task.id?c}"
                                     onclick="window.location.href='/tasks/${task.id?c}'">

                                    <div class="task-card-id">#${task.id?c}</div>
                                    <div class="task-card-title">
                                        <a href="/tasks/${task.id?c}" class="task-link" onclick="event.stopPropagation();">
                                            ${task.name}
                                        </a>
                                    </div>

                                    <div class="task-card-meta">
                                        <#if task.assigneeName??>
                                            <span class="task-card-assignee">${task.assigneeName}</span>
                                        </#if>
                                        <#if task.priority??>
                                            <span class="task-card-priority priority-${task.priority?lower_case}">
                                                ${task.priority}
                                            </span>
                                        </#if>
                                    </div>

                                    <#if task.deadline??>
                                        <div class="task-card-deadline">${task.deadline}</div>
                                    </#if>

                                    <div class="task-card-status">
                                        <span class="status-badge status-${(task.status!'TODO')?lower_case}">
                                            ${task.status?replace("_", " ")}
                                        </span>
                                    </div>
                                </div>
                            </#if>
                        </#list>
                    </div>

                    <#if !(tasks?filter(t -> t.status == status)?size gt 0)>
                        <div class="kanban-column-empty" id="empty-${status}">Нет задач</div>
                    </#if>
                </div>
            </#list>
        </div>
    </div>

    <div id="createTaskModal" class="modal">
        <div class="modal-content">
            <span class="modal-close" onclick="hideCreateTaskModal()">&times;</span>
            <h3>Новая задача</h3>

            <form id="quickTaskForm">
                <label>Название задачи <span class="required">*</span></label>
                <input type="text" id="quickTaskName" required placeholder="Введите название">

                <label>Приоритет</label>
                <select id="quickTaskPriority">
                    <option value="MEDIUM">Средний</option>
                    <option value="LOW">Низкий</option>
                    <option value="HIGH">Высокий</option>
                    <option value="CRITICAL">Критический</option>
                </select>

                <div class="modal-buttons">
                    <button type="button" onclick="hideCreateTaskModal()" class="btn">Отмена</button>
                    <button type="button" onclick="createQuickTask()" class="btn btn-primary">Создать</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        let currentColumnStatus = 'TODO';

        function getCsrfToken() {
            return document.querySelector('meta[name="_csrf"]').content;
        }

        function getCsrfHeader() {
            return document.querySelector('meta[name="_csrf_header"]').content;
        }

        function showCreateTaskModal(status) {
            currentColumnStatus = status;
            document.getElementById('quickTaskName').value = '';
            document.getElementById('createTaskModal').classList.add('show');
            document.getElementById('quickTaskName').focus();
        }

        function hideCreateTaskModal() {
            document.getElementById('createTaskModal').classList.remove('show');
        }

        function createQuickTask() {
            const name = document.getElementById('quickTaskName').value.trim();
            if (!name) {
                alert("Название задачи обязательно");
                return;
            }

            const priority = document.getElementById('quickTaskPriority').value;

            const taskDto = {
                name: name,
                status: currentColumnStatus,
                priority: priority,
                projectId: ${project.id?c}
            };

            fetch('/api/tasks', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    [getCsrfHeader()]: getCsrfToken()
                },
                body: JSON.stringify(taskDto)
            })
                .then(response => {
                    if (response.ok) {
                        hideCreateTaskModal();
                        location.reload();
                    } else if (response.status === 403) {
                        alert('Ошибка доступа. Только менеджеры и администраторы могут создавать задачи.');
                    } else {
                        alert('Не удалось создать задачу');
                    }
                })
                .catch(() => alert('Ошибка соединения'));
        }

        // Drag & Drop
        document.querySelectorAll('.task-card').forEach(card => {
            card.addEventListener('dragstart', e => {
                e.dataTransfer.setData('text/plain', card.dataset.taskId);
                card.classList.add('dragging');
            });
            card.addEventListener('dragend', () => card.classList.remove('dragging'));
        });

        document.querySelectorAll('.kanban-column').forEach(column => {
            column.addEventListener('dragover', e => { e.preventDefault(); column.classList.add('drag-over'); });
            column.addEventListener('dragleave', () => column.classList.remove('drag-over'));
            column.addEventListener('drop', e => {
                e.preventDefault();
                column.classList.remove('drag-over');
                const taskId = e.dataTransfer.getData('text/plain').trim();
                const newStatus = column.dataset.status;
                if (!taskId || !newStatus) return;
                fetch('/dashboard/tasks/' + taskId + '/status?status=' + newStatus, {
                    method: 'POST',
                    headers: { [getCsrfHeader()]: getCsrfToken() }
                }).then(() => location.reload());
            });
        });

        window.onclick = function(event) {
            const modal = document.getElementById('createTaskModal');
            if (event.target === modal) hideCreateTaskModal();
        };
    </script>
</div>
</html>