<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title><#if task.getId()??>Редактирование задачи<#else>Новая задача</#if></title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body class="container-form">

<h1>
    <#if task.getId()??>Редактирование задачи<#else>Новая задача</#if>
</h1>

<#if error??>
    <div class="error">${error}</div>
</#if>

<form id="taskForm" method="post"
      action="<#if task.getId()??>/tasks/${task.getId()?c}/update<#else>/tasks</#if>">

    <#if task.getId()??>
        <input type="hidden" name="id" value="${task.getId()?c}">
    </#if>

    <label>Название задачи <span class="required">*</span></label>
    <input type="text" name="name" value="${task.getName()!''}" required>

    <label>Описание</label>
    <textarea name="description" rows="4">${task.getDescription()!''}</textarea>

    <label>Проект <span class="required">*</span></label>
    <select name="projectId" id="projectSelect" required onchange="changeProject(this.value)">
        <option value="">-- Выберите проект --</option>
        <#list projects as project>
            <option value="${project.getId()}"
                    <#if (selectedProjectId?? && selectedProjectId == project.getId()) ||
                    (task.getProjectId()?? && task.getProjectId() == project.getId())>selected</#if>>
                ${project.getName()!''}
            </option>
        </#list>
    </select>

    <label>Спринт</label>
    <select name="sprintId" id="sprintSelect">
        <option value="">-- Без спринта --</option>
        <#list sprints as sprint>
            <option value="${sprint.getId()}"
                    <#if task.getSprintId()?? && task.getSprintId() == sprint.getId()>selected</#if>>
                ${sprint.getName()!''}
                <#if sprint.getStartDate()?? && sprint.getEndDate()??>
                    (${sprint.getStartDate()} — ${sprint.getEndDate()})
                </#if>
            </option>
        </#list>
    </select>

    <label>Исполнитель</label>
    <select name="assigneeId">
        <option value="">-- Не назначен --</option>
        <#list users as user>
            <option value="${user.getId()}"
                    <#if task.getAssigneeId()?? && task.getAssigneeId() == user.getId()>selected</#if>>
                ${user.getName()!''}
            </option>
        </#list>
    </select>

    <label>Приоритет</label>
    <select name="priority">
        <option value="">-- Выберите приоритет --</option>
        <option value="LOW"      <#if task.getPriority()?? && task.getPriority() == 'LOW'>selected</#if>>Низкий</option>
        <option value="MEDIUM"   <#if task.getPriority()?? && task.getPriority() == 'MEDIUM'>selected</#if>>Средний</option>
        <option value="HIGH"     <#if task.getPriority()?? && task.getPriority() == 'HIGH'>selected</#if>>Высокий</option>
        <option value="CRITICAL" <#if task.getPriority()?? && task.getPriority() == 'CRITICAL'>selected</#if>>Критический</option>
    </select>

    <label>Статус</label>
    <select name="status">
        <option value="TODO"        <#if task.getStatus()?? && task.getStatus() == 'TODO'>selected</#if>>To Do</option>
        <option value="IN_PROGRESS" <#if task.getStatus()?? && task.getStatus() == 'IN_PROGRESS'>selected</#if>>In Progress</option>
        <option value="DONE"        <#if task.getStatus()?? && task.getStatus() == 'DONE'>selected</#if>>Done</option>
        <option value="BLOCKED"     <#if task.getStatus()?? && task.getStatus() == 'BLOCKED'>selected</#if>>Blocked</option>
    </select>

    <label>Дедлайн</label>
    <input type="date"
           name="deadline"
           value="${task.getDeadline()!''}">

    <button type="submit">
        <#if task.getId()??>Сохранить изменения<#else>Создать задачу</#if>
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