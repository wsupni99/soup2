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
                <label>Название <span class="required">*</span></label>
                <input type="text" id="quickTaskName" required>

                <label>Описание</label>
                <textarea id="quickTaskDescription" rows="3" placeholder="Краткое описание задачи"></textarea>

                <label>Исполнитель</label>
                <select id="quickTaskAssignee">
                    <option value="">— Не назначен —</option>
                </select>

                <label>Дедлайн</label>
                <input type="date" id="quickTaskDeadline">

                <label>Спринт</label>
                <select id="quickTaskSprint">
                    <option value="">— Без спринта —</option>
                </select>

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
        function getCsrfToken() {
            return document.querySelector('meta[name="_csrf"]').content;
        }

        function getCsrfHeader() {
            return document.querySelector('meta[name="_csrf_header"]').content;
        }

        function hideCreateTaskModal() {
            document.getElementById('createTaskModal').classList.remove('show');
        }

        let currentColumnStatus = 'TODO';

        function showCreateTaskModal(status) {
            currentColumnStatus = status;
            document.getElementById('quickTaskForm').reset();
            document.getElementById('createTaskModal').classList.add('show');
            document.getElementById('quickTaskName').focus();

            const projectId = ${project.id?c};
            loadProjectUsers(projectId);
            loadProjectSprints(projectId);
        }

        function loadProjectUsers(projectId) {
            fetch('/users/byProject?projectId=' + projectId)
                .then(r => r.ok ? r.json() : [])
                .then(users => {
                    const select = document.getElementById('quickTaskAssignee');
                    select.innerHTML = '<option value="">— Не назначен —</option>';
                    users.forEach(u => {
                        const opt = document.createElement('option');
                        opt.value = u.userId || u.id;
                        opt.textContent = u.name + (u.email ? ' (' + u.email + ')' : '');
                        select.appendChild(opt);
                    });
                });
        }

        function loadProjectSprints(projectId) {
            fetch('/sprints/byProject?projectId=' + projectId)
                .then(r => r.ok ? r.json() : [])
                .then(sprints => {
                    const select = document.getElementById('quickTaskSprint');
                    select.innerHTML = '<option value="">— Без спринта —</option>';
                    sprints.forEach(s => {
                        const opt = document.createElement('option');
                        opt.value = s.id;
                        opt.textContent = s.name;
                        select.appendChild(opt);
                    });
                });
        }

        function createQuickTask() {
            const name = document.getElementById('quickTaskName').value.trim();
            if (!name) {
                alert("Название обязательно");
                return;
            }

            const taskDto = {
                name: name,
                description: document.getElementById('quickTaskDescription').value.trim(),
                status: currentColumnStatus,
                priority: document.getElementById('quickTaskPriority').value,
                projectId: ${project.id?c},
                assigneeId: document.getElementById('quickTaskAssignee').value ?
                    parseInt(document.getElementById('quickTaskAssignee').value) : null,
                sprintId: document.getElementById('quickTaskSprint').value ?
                    parseInt(document.getElementById('quickTaskSprint').value) : null,
                deadline: document.getElementById('quickTaskDeadline').value || null
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
                    } else {
                        return response.text().then(text => {
                            console.error('Server error:', text);
                            alert('Ошибка создания задачи: ' + (text || response.status));
                        });
                    }
                })
                .catch(err => {
                    console.error('Fetch error:', err);
                    alert('Ошибка соединения. Задача могла быть создана (проверьте список).');
                    location.reload();
                });
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