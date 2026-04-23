<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Уведомление</title>
</head>
<body>
<h3>Здравствуйте!</h3>
<p>В задаче <strong>«${taskName}»</strong> произошло изменение: <em>${action}</em>.</p>
<p>Текущий статус: <strong>${taskStatus}</strong></p>
<#if taskAssignee??>
    <p>Исполнитель: ${taskAssignee}</p>
</#if>
<p>Ссылка: <a href="${taskLink}">Открыть задачу</a></p>
<hr>
<p>С уважением,<br/>Система управления проектами</p>
</body>
</html>