<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Tasks Dashboard</title>
</head>
<body>
<h1>Tasks Dashboard</h1>
<a href="/projects">Projects (Manager+)</a> |
<a href="/sprints">Sprints (Manager+)</a> |
<a href="/admin/users">Users (Admin only)</a>

<h2>All Tasks</h2>
<table border="1">
    <tr><th>ID</th><th>Name</th><th>Status</th><th>Priority</th><th>Project</th></tr>
    <#list tasks as task>
        <tr>
            <td>${task.id}</td>
            <td>${task.name}</td>
            <td>${task.status!}</td>
            <td>${task.priority!}</td>
            <td>${task.project.name!}</td>
        </tr>
    </#list>
</table>
</body>
</html>