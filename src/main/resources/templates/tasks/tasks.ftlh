<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Задачи</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<h1>Управление задачами</h1>

<a href="/tasks/new" class="btn btn-primary">Новая задача</a>
<a href="/sprints" class="btn">К спринтам</a>
<a href="/projects" class="btn">К проектам</a>
<#if currentUserRole == "ROLE_ADMIN">
    <a href="/admin/users" class="btn">Админ: users</a>
    <a href="/admin/tasklogs" class="btn">Админ: tasklogs</a>
</#if>

<div class="filter-form">
    <form method="get" action="/tasks" id="filterForm">
        <div class="filter-row">
            <div class="filter-group">
                <label>Поиск:</label>
                <input type="text" name="search" value="${selectedSearch!''}"
                       placeholder="Название или описание задачи...">
            </div>

            <div class="filter-group">
                <label>Проект:</label>
                <select name="projectId" id="projectSelect">
                    <option value="">Все проекты</option>
                    <#list projects as project>
                        <option value="${project.id}"
                                <#if selectedProjectId?? && selectedProjectId == project.id>selected</#if>>
                            ${project.name!''}
                        </option>
                    </#list>
                </select>
            </div>

            <div class="filter-group">
                <label>Спринт:</label>
                <select name="sprintId" id="sprintSelect">
                    <option value="">Все спринты</option>
                    <#list sprints as sprint>
                        <option value="${sprint.id}"
                                <#if selectedSprintId?? && selectedSprintId == sprint.id>selected</#if>>
                            ${sprint.name!''}
                        </option>
                    </#list>
                </select>
            </div>

            <div class="filter-group">
                <label>Статус:</label>
                <select name="status">
                    <option value="">Все</option>
                    <option value="TODO" <#if selectedStatus?? && selectedStatus == 'TODO'>selected</#if>>To Do</option>
                    <option value="IN_PROGRESS" <#if selectedStatus?? && selectedStatus == 'IN_PROGRESS'>selected</#if>>In Progress</option>
                    <option value="DONE" <#if selectedStatus?? && selectedStatus == 'DONE'>selected</#if>>Done</option>
                    <option value="BLOCKED" <#if selectedStatus?? && selectedStatus == 'BLOCKED'>selected</#if>>Blocked</option>
                </select>
            </div>

            <div class="filter-group">
                <label>Приоритет:</label>
                <select name="priority">
                    <option value="">Все</option>
                    <option value="LOW" <#if selectedPriority?? && selectedPriority == 'LOW'>selected</#if>>Низкий</option>
                    <option value="MEDIUM" <#if selectedPriority?? && selectedPriority == 'MEDIUM'>selected</#if>>Средний</option>
                    <option value="HIGH" <#if selectedPriority?? && selectedPriority == 'HIGH'>selected</#if>>Высокий</option>
                    <option value="CRITICAL" <#if selectedPriority?? && selectedPriority == 'CRITICAL'>selected</#if>>Критический</option>
                </select>
            </div>

            <div class="filter-group">
                <label>Исполнитель:</label>
                <select name="assigneeId" id="filterAssignee">
                    <option value="">Все исполнители</option>
                </select>
            </div>
        </div>

        <div style="margin-top: 20px;">
            <button type="submit" class="btn btn-primary">Применить фильтры</button>
            <a href="/tasks" class="btn" style="background: #6c757d; color: white;">Сбросить все фильтры</a>
        </div>
    </form>
</div>

<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Название задачи</th>
        <th>Проект</th>
        <th>Спринт</th>
        <th>Приоритет</th>
        <th>Статус</th>
        <th>Дедлайн</th>
        <th>Исполнитель</th>
    </tr>
    </thead>
    <tbody>
    <#list tasks as task>
        <tr>
            <td><strong>#${task.id!''}</strong></td>

            <td>
                <a href="/tasks/${task.id?c}" class="task-link">
                    ${task.name!''}
                </a>
            </td>

            <td>${task.projectName!''}</td>
            <td>${task.sprintName!''}</td>

            <td>
                <#if task.priority??>
                    <span class="priority-badge priority-${task.priority?lower_case}">
                            ${task.priority}
                        </span>
                <#else>
                    —
                </#if>
            </td>

            <td>
                <#if task.status??>
                    <span class="status-badge status-${task.status?lower_case}">
                            ${task.status}
                        </span>
                <#else>
                    —
                </#if>
            </td>

            <td>${task.deadline! '—'}</td>

            <td>
                <#if task.assigneeName?? && task.assigneeName?has_content>
                    ${task.assigneeName}
                <#else>
                    <span style="color: #999;">—</span>
                </#if>
            </td>
        </tr>
    </#list>
    </tbody>
</table>

<script>
    document.getElementById('projectSelect').addEventListener('change', function() {
        const projectId = this.value;
        const assigneeSelect = document.getElementById('filterAssignee');

        assigneeSelect.innerHTML = '<option value="">Все исполнители</option>';

        if (!projectId) return;

        fetch('/users/byProject?projectId=' + projectId)
            .then(r => r.ok ? r.json() : [])
            .then(users => {
                users.forEach(u => {
                    const opt = document.createElement('option');
                    opt.value = u.id;
                    opt.textContent = u.name;
                    assigneeSelect.appendChild(opt);
                });
            });
    });
</script>
</body>
</html>