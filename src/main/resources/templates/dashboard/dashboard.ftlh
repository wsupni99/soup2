<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <title>Дашборд — ${project.name!''}</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<div class="dashboard-container">
    <div class="dashboard-header">
        <h1>Дашборд проекта: ${project.name}</h1>
    </div>

    <#-- Переключение проектов -->
    <#if userProjects?has_content>
        <div class="project-switch">
            <#list userProjects as p>
                <a href="/dashboard/${p.id}"
                   class="btn ${(p.id == project.id)?string('btn-primary', '')}">
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
                    <span class="task-count">
                        <#assign count = 0>
                        <#list tasks as task>
                            <#if task.status == status>
                                <#assign count++>
                            </#if>
                        </#list>
                        ${count}
                    </span>
                </div>
                <#assign hasContent = false>
                <#list tasks as task>
                    <#if task.status == status>
                        <#assign hasContent = true>
                        <div class="task-card"
                             draggable="true"
                             data-task-id="${task.id?c}"
                             onclick="window.location.href='/tasks/${task.id?c}'">
                            <div class="task-card-id">#${task.id?c}</div>
                            <div class="task-card-title">
                                <a href="/tasks/${task.id?c}"
                                   class="task-link"
                                   onclick="event.stopPropagation();">
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
                                <div class="task-card-deadline">
                                    ${task.deadline}
                                </div>
                            </#if>
                        </div>
                    </#if>
                </#list>
                <#if !hasContent>
                    <div class="kanban-column-empty">Нет задач</div>
                </#if>
            </div>
        </#list>
    </div>
</div>

<script>
    document.querySelectorAll('.task-card').forEach(card => {
        card.addEventListener('dragstart', e => {
            e.dataTransfer.setData('text/plain', card.dataset.taskId.trim());
            card.classList.add('dragging');
        });

        card.addEventListener('dragend', e => {
            card.classList.remove('dragging');
        });
    });

    document.querySelectorAll('.kanban-column').forEach(column => {
        column.addEventListener('dragover', e => {
            e.preventDefault();
            column.classList.add('drag-over');
        });

        column.addEventListener('dragleave', e => {
            column.classList.remove('drag-over');
        });

        column.addEventListener('drop', e => {
            e.preventDefault();
            column.classList.remove('drag-over');

            const taskId = e.dataTransfer.getData('text/plain').trim();
            const newStatus = column.dataset.status;
            const csrfToken = document.querySelector('meta[name="_csrf"]').content;

            fetch('/dashboard/tasks/' + taskId + '/status?status=' + newStatus, {
                method: 'POST',
                headers: {
                    'X-CSRF-TOKEN': csrfToken,
                    'Content-Type': 'application/json'
                }
            })
                .then(response => {
                    if (response.ok) {
                        window.location.reload();
                    } else {
                        console.error('Failed to update task status');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        });
    });
</script>
</body>
</html>