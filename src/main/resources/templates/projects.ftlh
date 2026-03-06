<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Projects</title>
</head>
<body>
<h1>Projects</h1>

<ul>
    <#list projects as project>
        <li>${project.name} - ${project.description}</li>
    </#list>
</ul>

</body>
</html>
