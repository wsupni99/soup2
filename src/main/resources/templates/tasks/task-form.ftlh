<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title><#if task.id??>Редактирование задачи<#else>Новая задача</#if></title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body class="container-form">

<h1>
    <#if task.id??>Редактирование задачи<#else>Новая задача</#if>
</h1>

<#if error??>
    <div class="error">${error}</div>
</#if>

<form id="taskForm" method="post"
      action="<#if task.id??>/tasks/${task.id?c}/update<#else>/tasks</#if>">

    <#if task.id??>
        <input type="hidden" name="id" value="${task.id?c}">
    </#if>
    <input type="hidden" name="_csrf" value="${_csrf.token}">
    <label>Название задачи <span class="required">*</span></label>
    <input type="text" name="name" value="${task.name!''}" required>

    <label>Описание</label>
    <textarea name="description" rows="4">${task.description!''}</textarea>

    <label>Проект <span class="required">*</span></label>
    <select name="projectId" id="projectSelect" required onchange="changeProject(this.value)">
        <option value="">-- Выберите проект --</option>
        <#list projects as project>
            <option value="${project.id}"
                    <#if (task.projectId?? && task.projectId == project.id) || (selectedProjectId?? && selectedProjectId == project.id)>selected</#if>>
                ${project.name!''}
            </option>
        </#list>
    </select>

    <label>Спринт</label>
    <select name="sprintId" id="sprintSelect">
        <option value="">-- Без спринта --</option>
        <#list sprints as sprint>
            <option value="${sprint.id}"
                    <#if task.sprintId?? && task.sprintId == sprint.id>selected</#if>>
                ${sprint.name!''}
                <#if sprint.startDate?? && sprint.endDate??>
                    (${sprint.startDate} — ${sprint.endDate})
                </#if>
            </option>
        </#list>
    </select>

    <label>Исполнитель</label>
    <select name="assigneeId">
        <option value="">-- Не назначен --</option>
        <#list users as user>
            <option value="${user.id}"
                    <#if task.assigneeId?? && task.assigneeId == user.id>selected</#if>>
                ${user.name!''}
            </option>
        </#list>
    </select>

    <label>Приоритет</label>
    <select name="priority">
        <option value="">-- Выберите приоритет --</option>
        <option value="LOW"      <#if task.priority?? && task.priority == 'LOW'>selected</#if>>Низкий</option>
        <option value="MEDIUM"   <#if task.priority?? && task.priority == 'MEDIUM'>selected</#if>>Средний</option>
        <option value="HIGH"     <#if task.priority?? && task.priority == 'HIGH'>selected</#if>>Высокий</option>
        <option value="CRITICAL" <#if task.priority?? && task.priority == 'CRITICAL'>selected</#if>>Критический</option>
    </select>

    <label>Статус</label>
    <select name="status">
        <option value="TODO"        <#if task.status?? && task.status == 'TODO'>selected</#if>>To Do</option>
        <option value="IN_PROGRESS" <#if task.status?? && task.status == 'IN_PROGRESS'>selected</#if>>In Progress</option>
        <option value="DONE"        <#if task.status?? && task.status == 'DONE'>selected</#if>>Done</option>
        <option value="BLOCKED"     <#if task.status?? && task.status == 'BLOCKED'>selected</#if>>Blocked</option>
    </select>

    <label>Дедлайн</label>
    <input type="date" name="deadline" value="${task.deadline!''}">

    <button type="submit">
        <#if task.id??>Сохранить изменения<#else>Создать задачу</#if>
    </button>
</form>

<br>
<a href="/tasks" class="btn btn-primary">← Назад к списку задач</a>

<script>
    function changeProject(projectId) {
        const sprintSelect = document.getElementById('sprintSelect');
        sprintSelect.innerHTML = '<option value="">-- Без спринта --</option>';

        if (!projectId) return;

        fetch('/sprints/byProject?projectId=' + projectId)
            .then(response => {
                if (!response.ok) throw new Error('Ошибка сервера');
                return response.json();
            })
            .then(data => {
                data.forEach(sprint => {
                    const option = document.createElement('option');
                    option.value = sprint.id;
                    option.textContent = sprint.name;
                    if (sprint.startDate && sprint.endDate) {
                        option.textContent += ' (' + sprint.startDate + ' — ' + sprint.endDate + ')';
                    }
                    sprintSelect.appendChild(option);
                });
            })
            .catch(error => {
                console.error('Ошибка загрузки спринтов:', error);
            });
    }
</script>

</body>
</html>