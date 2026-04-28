<header class="main-header">
    <div class="header-inner">
        <div class="header-left">
            <a href="/dashboard" class="logo">SOUP</a>
        </div>

        <nav class="main-nav">
            <a href="/tasks" class="nav-link">Задачи</a>
            <a href="/projects" class="nav-link">Проекты</a>
            <a href="/sprints" class="nav-link">Спринты</a>
            <a href="/dashboard" class="nav-link">Дашборд</a>
        </nav>

        <div class="header-right">
            <#if currentUserRole?? && currentUserRole == "ROLE_ADMIN">
                <a href="/admin/users" class="nav-link admin-link">Юзеры</a>
                <a href="/admin/tasklogs" class="nav-link admin-link">Тасклог</a>
            </#if>

            <div class="user-info">
                <span class="username">
                    <#if userDetails?? && userDetails.user?? && userDetails.user.email??>
                        ${userDetails.user.email}
                    <#else>
                        Пользователь
                    </#if>
                </span>
                <a href="/logout" class="btn btn-logout">Выйти</a>
            </div>
        </div>
    </div>
</header>