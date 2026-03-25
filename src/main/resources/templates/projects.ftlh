<h1>Projects</h1>
<a href="/projects/new">+ New Project</a>
<table>
    <#list projects as p>
        <tr>
            <td>${p.name}</td>
            <td><a href="/projects/${p.id}/edit">Edit</a></td>
        </tr>
    </#list>
</table>