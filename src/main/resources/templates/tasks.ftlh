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
        .filter-form { background: #f9f9f9; padding: 15px; border-radius: 8px; margin-bottom: 20px; }
        .error { color: red; background: #ffe6e6; padding: 12px; border-radius: 6px; margin: 10px 0; }
    </style>
</head>
<body>
<h1>Управление задачами</h1>

<#if error??>
    <div class="error">${error}</div>
</#if>

<!-- Фильтры -->
<div class="filter-form">
    <form method="get" action="/tasks">
        <label>Проект:
            <select name="projectId">
                <option value="">Все проекты</option>
                <#list projects as project>
                    <option value="${project.id}"
                            <#if selectedProjectId?? && selectedProjectId == project.id>selected</#if>>
                        ${project.name!''}
                    </option>
                </#list>
            </select>
        </label>

        <label>Спринт:
            <select name="sprintId">
                <option value="">Все спринты</option>
                <#list sprints as sprint>
                    <option value="${sprint.id}"
                            <#if selectedSprintId?? && selectedSprintId == sprint.id>selected</#if>>
                        ${sprint.name!''}
                    </option>
                </#list>
            </select>
        </label>

        <label>Статус:
            <select name="status">
                <option value="">Все</option>
                <option value="TODO" <#if selectedStatus?? && selectedStatus == 'TODO'>selected</#if>>To Do</option>
                <option value="IN_PROGRESS" <#if selectedStatus?? && selectedStatus == 'IN_PROGRESS'>selected</#if>>In Progress</option>
                <option value="DONE" <#if selectedStatus?? && selectedStatus == 'DONE'>selected</#if>>Done</option>
                <option value="BLOCKED" <#if selectedStatus?? && selectedStatus == 'BLOCKED'>selected</#if>>Blocked</option>
            </select>
        </label>

        <label>Приоритет:
            <select name="priority">
                <option value="">Все</option>
                <option value="LOW" <#if selectedPriority?? && selectedPriority == 'LOW'>selected</#if>>Низкий</option>
                <option value="MEDIUM" <#if selectedPriority?? && selectedPriority == 'MEDIUM'>selected</#if>>Средний</option>
                <option value="HIGH" <#if selectedPriority?? && selectedPriority == 'HIGH'>selected</#if>>Высокий</option>
                <option value="CRITICAL" <#if selectedPriority?? && selectedPriority == 'CRITICAL'>selected</#if>>Критический</option>
            </select>
        </label>

        <label>Исполнитель:
            <select name="assigneeId">
                <option value="">Все исполнители</option>
                <#list users as user>
                    <option value="${user.id}"
                            <#if selectedAssigneeId?? && selectedAssigneeId == user.id>selected</#if>>
                        ${user.name!''}
                    </option>
                </#list>
            </select>
        </label>

        <button type="submit" class="btn btn-primary">Применить фильтры</button>
        <a href="/tasks" class="btn">Сбросить</a>
    </form>
</div>

<a href="/tasks/new" class="btn btn-primary">+ Новая задача</a>
<a href="/sprints" class="btn">← К спринтам</a>
<a href="/projects" class="btn">К проектам</a>

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
        <th>Действия</th>
    </tr>
    <#list tasks as task>
        <tr>
            <td>${task.id()!''}</td>
            <td>${task.name()!''}</td>
            <td>${task.projectName()!''}</td>
            <td>${task.sprintName()!''}</td>
            <td>${task.priority()!''}</td>
            <td>${task.status()!''}</td>
            <td>${task.deadline()!''}</td>
            <td>
                <#if task.assigneeName()?? && task.assigneeName()?has_content>
                    ${task.assigneeName()}
                <#else>
                    <span style="color: #999;">—</span>
                </#if>
            </td>
            <td>
                <a href="/tasks/${task.id()?c}/edit" class="btn btn-edit">Редактировать</a>
                <a href="/tasks/${task.id()?c}/delete"
                   class="btn btn-delete"
                   onclick="return confirm('Вы уверены, что хотите удалить задачу?')">
                    Удалить
                </a>
            </td>
        </tr>
    </#list>
</table>

</body>
</html>