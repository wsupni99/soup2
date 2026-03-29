<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Задачи</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h1 { color: #333; }
        table { width: 100%; border-collapse: collapse; margin: 20px 0; }
        th, td { border: 1px solid #ccc; padding: 10px; text-align: left; vertical-align: top; }
        th { background: #f0f0f0; }
        .btn {
            padding: 8px 12px;
            text-decoration: none;
            border-radius: 4px;
            margin-right: 5px;
            display: inline-block;
        }
        .btn-primary { background: #4e73df; color: white; }
        .btn-edit { background: #f39c12; color: white; }
        .btn-delete { background: #e74c3c; color: white; }
        .filter-form {
            background: #f9f9f9;
            padding: 20px;
            border-radius: 8px;
            margin-top: 15px;
            border: 1px solid #ddd;
        }
        .filter-row {
            display: flex;
            flex-wrap: wrap;
            gap: 30px;
            align-items: end;
        }
        .filter-group {
            display: flex;
            flex-direction: column;
            min-width: 180px;
        }
        .filter-group label {
            margin-bottom: 5px;
            font-weight: 600;
            font-size: 0.95em;
        }
        select, input[type="text"] {
            padding: 8px 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
            width: 100%;
        }
        .success-message {
            color: #155724;
            background: #d4edda;
            border: 1px solid #c3e6cb;
            padding: 12px;
            border-radius: 6px;
            margin: 10px 0;
        }
        .error {
            color: red;
            background: #ffe6e6;
            padding: 12px;
            border-radius: 6px;
            margin: 10px 0;
        }
    </style>
</head>
<body>
<h1>Управление задачами</h1>

<#if error??>
    <div class="error">${error}</div>
</#if>

<#if successMessage??>
    <div class="success-message">
        ${successMessage}
    </div>
</#if>

<a href="/tasks/new" class="btn btn-primary">+ Новая задача</a>
<a href="/sprints" class="btn">← К спринтам</a>
<a href="/projects" class="btn">К проектам</a>

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
                        <option value="${project.getId()}"
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
                        <option value="${sprint.getId()}"
                                <#if selectedSprintId?? && selectedSprintId == sprint.getId()>selected</#if>>
                            ${sprint.getName()!''}
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
                <select name="assigneeId">
                    <option value="">Все исполнители</option>
                    <#list users as user>
                        <option value="${user.getId()}"
                                <#if selectedAssigneeId?? && selectedAssigneeId == user.getId()>selected</#if>>
                            ${user.name!''}
                            <#if user.email?? && user.email?has_content>(${user.email})</#if>
                        </option>
                    </#list>
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
    <tr>
        <th>ID</th>
        <th>Название</th>
        <th>Проект</th>
        <th>Спринт</th>
        <th>Приоритет</th>
        <th>Статус</th>
        <th>Дедлайн</th>
        <th>Исполнитель</th>
        <#if currentUserRole == "ROLE_ADMIN" || currentUserRole == "ROLE_MANAGER">
            <th>Действия</th>
        </#if>
    </tr>
    <#list tasks as task>
        <tr>
            <td>${task.id!''}</td>
            <td>${task.name!''}</td>
            <td>${task.projectName!''}</td>
            <td>${task.sprintName!''}</td>
            <td>${task.priority!''}</td>
            <td>${task.status!''}</td>
            <td>${task.deadline!''}</td>
            <td>
                <#if task.assigneeName?? && task.assigneeName?has_content>
                    ${task.assigneeName}
                <#else>
                    <span style="color: #999;">—</span>
                </#if>
            </td>
            <#if currentUserRole == "ROLE_ADMIN" || currentUserRole == "ROLE_MANAGER">
                <td>
                    <a href="/tasks/${task.id?c}/edit" class="btn btn-edit">Редактировать</a>
                    <a href="/tasks/${task.id?c}/delete"
                       class="btn btn-delete"
                       onclick="return confirm('Вы уверены, что хотите удалить задачу?')">
                        Удалить
                    </a>
                </td>
            </#if>
        </tr>
    </#list>
</table>

<script>
    document.getElementById('projectSelect').addEventListener('change', function() {
        const projectId = this.value;
        const sprintSelect = document.getElementById('sprintSelect');
        sprintSelect.innerHTML = '<option value="">Все спринты</option>';
        if (!projectId) return;
    });
</script>

</body>
</html>