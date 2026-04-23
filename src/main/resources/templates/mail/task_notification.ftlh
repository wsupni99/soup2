<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Уведомление по задаче</title>
</head>
<body>
<h3>Уважаемый пользователь!</h3>
<p>По задаче <strong>${taskName}</strong> произошло событие:</p>
<p><em>${action}</em></p>
<ul>
    <li>Статус: ${taskStatus}</li>
    <li>Исполнитель: ${taskAssignee}</li>
</ul>
<p><a href="${taskLink}">Перейти к задаче</a></p>
<hr>
<small>Это автоматическое сообщение, пожалуйста, не отвечайте на него.</small>
</body>
</html>