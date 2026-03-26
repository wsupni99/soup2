<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>${sprint.id?? ? 'Редактирование спринта' : 'Новый спринт'}</title>

    <#-- Настройка форматирования дат -->
    <#assign dateFormat = "yyyy-MM-dd">

    <style>
        body { font-family: Arial, sans-serif; max-width: 600px; margin: 40px auto; padding: 20px; }
        form { display: flex; flex-direction: column; gap: 15px; }
        label { font-weight: bold; margin-top: 10px; }
        input, select { padding: 10px; border: 1px solid #ccc; border-radius: 6px; font-size: 16px; }
        button {
            padding: 12px;
            background: #4e73df;
            color: white;
            border: none;
            border-radius: 6px;
            font-size: 16px;
            cursor: pointer;
            margin-top: 20px;
        }
    </style>
</head>
<body>
<h1>${sprint.id?? ? 'Редактирование спринта' : 'Новый спринт'}</h1>

<form method="post"
      action="${sprint.id?? ? '/sprints/' + sprint.id + '/update' : '/sprints'}">

    <label>Название спринта *</label>
    <input type="text" name="name" value="${(sprint.name)!}" required>

    <label>ID Проекта *</label>
    <input type="number" name="projectId" value="${(sprint.projectId)!}" required>

    <label>Дата начала</label>
    <input type="date" name="startDate"
           value="${(sprint.startDate?string(dateFormat))!''}" required>

    <label>Дата окончания</label>
    <input type="date" name="endDate"
           value="${(sprint.endDate?string(dateFormat))!''}" required>

    <button type="submit">Сохранить спринт</button>
</form>

<a href="/sprints" style="margin-top:20px; display:inline-block;">← Назад к спринтам</a>
</body>
</html>