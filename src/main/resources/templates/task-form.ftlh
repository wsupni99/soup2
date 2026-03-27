<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title><#if task.id()??>Редактирование задачи<#else>Новая задача</#if></title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 800px; margin: 40px auto; padding: 20px; }
        form { display: flex; flex-direction: column; gap: 15px; }
        label { font-weight: bold; margin-top: 8px; }
        input, select, textarea {
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 6px;
            font-size: 16px;
        }
        button {
            padding: 12px;
            background: #4e73df;
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 16px;
        }
        button:hover { background: #3a5bc7; }
        .error {
            color: #d32f2f;
            background: #ffe6e6;
            padding: 14px 16px;
            border-radius: 6px;
            margin: 15px 0;
            border-left: 5px solid #d32f2f;
        }
        .required { color: red; }
    </style>
</head>
<body>

<h1>
    <#if task.id()??>Редактирование задачи<#else>Новая задача</#if>
</h1>

<#if error??>
    <div class="error">${error}</div>
</#if>

<form id="taskForm" method="post"
      action="<#if task.id()??>/tasks/${task.id()?c}/update<#else>/tasks</#if>">

    <#if task.id()??>
        <input type="hidden" name="id" value="${task.id()}">
    </#if>

    <label>Название задачи <span class="required">*</span></label>
    <input type="text" name="name" value="${task.name()!''}" required>

    <label>Описание</label>
    <textarea name="description" rows="4">${task.description()!''}</textarea>
    <br>
    <div class="error">Рекомендуем сначала выбрать проект!</div>
    <label>Проект <span class="required">*</span></label>
    <select name="projectId" id="projectSelect" required onchange="changeProject(this.value)">
        <option value="">-- Выберите проект --</option>
        <#list projects as project>
            <option value="${project.id}"
                    <#if (selectedProjectId?? && selectedProjectId == project.id) ||
                    (task.projectId()?? && task.projectId() == project.id)>selected</#if>>
                ${project.name!''}
            </option>
        </#list>
    </select>

    <label>Спринт</label>
    <select name="sprintId">
        <option value="">-- Без спринта --</option>
        <#list sprints as sprint>
            <option value="${sprint.id}"
                    <#if task.sprintId()?? && task.sprintId() == sprint.id>selected</#if>>
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
                    <#if task.assigneeId()?? && task.assigneeId() == user.id>selected</#if>>
                ${user.name!''}
            </option>
        </#list>
    </select>

    <label>Приоритет</label>
    <select name="priority">
        <option value="">-- Выберите приоритет --</option>
        <option value="LOW"      <#if task.priority()?? && task.priority() == 'LOW'>selected</#if>>Низкий</option>
        <option value="MEDIUM"   <#if task.priority()?? && task.priority() == 'MEDIUM'>selected</#if>>Средний</option>
        <option value="HIGH"     <#if task.priority()?? && task.priority() == 'HIGH'>selected</#if>>Высокий</option>
        <option value="CRITICAL" <#if task.priority()?? && task.priority() == 'CRITICAL'>selected</#if>>Критический</option>
    </select>

    <label>Статус</label>
    <select name="status">
        <option value="TODO"        <#if task.status()?? && task.status() == 'TODO'>selected</#if>>To Do</option>
        <option value="IN_PROGRESS" <#if task.status()?? && task.status() == 'IN_PROGRESS'>selected</#if>>In Progress</option>
        <option value="DONE"        <#if task.status()?? && task.status() == 'DONE'>selected</#if>>Done</option>
        <option value="BLOCKED"     <#if task.status()?? && task.status() == 'BLOCKED'>selected</#if>>Blocked</option>
    </select>

    <label>Дедлайн</label>
    <input type="date" name="deadline" value="${(deadlineStr)!''}">

    <button type="submit">
        <#if task.id()??>Сохранить изменения<#else>Создать задачу</#if>
    </button>
</form>

<br>
<a href="/tasks">← Назад к списку задач</a>

<script>
    function changeProject(projectId) {
        if (!projectId) return;
        let url = '/tasks/new?selectedProjectId=' + projectId;
        <#if task.id()??>
        url += '&id=${task.id()?c}';
        </#if>
        window.location.href = url;
    }
</script>

</body>
</html>