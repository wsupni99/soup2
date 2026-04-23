<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Задача #${task.id?c} — ${task.name}</title>
    <link rel="stylesheet" href="/css/style.css">

    <!-- CSRF защита -->
    <meta name="_csrf" content="${_csrf.token}">
    <meta name="_csrf_header" content="${_csrf.headerName}">
</head>
<body class="container-form">

<h1>Задача №${task.id?c}: ${task.name}</h1>

<a href="/tasks" class="btn btn-primary">← Назад к списку задач</a>

<div class="task-detail-card">
    <#if successMessage??>
        <div class="success-message">${successMessage}</div>
    </#if>
    <#if errorMessage??>
        <div class="error">${errorMessage}</div>
    </#if>

    <!-- Основная информация -->
    <div class="info-section">
        <h3>Информация о задаче</h3>
        <div class="info-grid">
            <div class="info-row">
                <span class="info-label">Проект:</span>
                <span class="info-value">${task.projectName! '—'}</span>
            </div>
            <div class="info-row">
                <span class="info-label">Спринт:</span>
                <span class="info-value">${task.sprintName! '—'}</span>
            </div>
            <div class="info-row">
                <span class="info-label">Статус:</span>
                <span class="status-badge status-${(task.status!'TODO')?lower_case}">
            ${task.status! 'TODO'}
        </span>
            </div>
            <div class="info-row">
                <span class="info-label">Приоритет:</span>
                <span class="priority-badge priority-${(task.priority!'MEDIUM')?lower_case}">
            ${task.priority! 'MEDIUM'}
        </span>
            </div>
            <div class="info-row">
                <span class="info-label">Дедлайн:</span>
                <span class="info-value">${task.deadline! '—'}</span>
            </div>
            <div class="info-row">
                <span class="info-label">Исполнитель:</span>
                <span class="info-value">${task.assigneeName! '—'}</span>
            </div>
        </div>
    </div>

    <!-- Описание -->
    <div class="description-section">
        <h3>Описание задачи</h3>
        <div class="description-content">
            <#if task.description?? && task.description?has_content>
                ${task.description?no_esc}
            <#else>
                <p class="empty">Описание отсутствует</p>
            </#if>
        </div>
    </div>

    <!-- Подзадачи -->
    <div class="subtasks-section">
        <div class="section-header">
            <h3>Подзадачи (${task.subTasks?size!0})</h3>
            <button onclick="showCreateSubtaskModal()" class="btn btn-primary">
                + Добавить подзадачу
            </button>
        </div>

        <div id="subtasks-list" class="subtask-grid">
            <#list task.subTasks as sub>
                <div class="subtask-card">
                    <div class="subtask-header">
                        <a href="/tasks/${sub.id?c}" class="subtask-link">
                            #${sub.id?c} — ${sub.name}
                        </a>
                    </div>
                    <div class="subtask-body">
                        <div>Исполнитель: <strong>${sub.assigneeName!'—'}</strong></div>
                        <div>Дедлайн: <strong>${sub.deadline! '—'}</strong></div>
                        <#if sub.status??>
                            <span class="status-badge status-${sub.status?lower_case}">${sub.status}</span>
                        </#if>
                    </div>
                </div>
            </#list>

            <#if !task.subTasks?? || task.subTasks?size == 0>
                <p class="empty">Подзадач пока нет</p>
            </#if>
        </div>
    </div>

    <!-- Модальное окно -->
    <div id="subtaskModal" class="modal">
        <div class="modal-content">
            <span class="modal-close" onclick="hideModal()">&times;</span>
            <h3>Создать подзадачу</h3>

            <form id="subtaskForm">
                <label>Название подзадачи <span class="required">*</span></label>
                <input type="text" id="subtaskName" required placeholder="Введите название">

                <label>Исполнитель</label>
                <select id="subtaskAssignee">
                    <option value="">— Не назначен —</option>
                </select>

                <label>Дедлайн</label>
                <input type="date" id="subtaskDeadline">

                <div class="modal-buttons">
                    <button type="button" onclick="hideModal()" class="btn">Отмена</button>
                    <button type="button" onclick="createSubtaskFromModal()" class="btn btn-primary">Создать</button>
                </div>
            </form>
        </div>
    </div>

    <!-- Прикреплённые файлы -->
    <div class="attachments-section">
        <h3>Прикреплённые файлы</h3>

        <div class="upload-area">
            <form id="uploadForm" enctype="multipart/form-data">
                <input type="file" name="file" id="fileInput" accept="image/*,.pdf,.doc,.docx,.txt">
                <button type="button" onclick="uploadFile()" class="btn btn-primary">Загрузить файл</button>
            </form>
        </div>

        <div id="attachments-list" class="attachment-list">
            <#list task.attachments as att>
                <div class="attachment-item">
                    <div class="attachment-preview">
                        <#if att.fileType?starts_with("image")>
                            <img src="${att.fileUrl}" alt="${att.fileName}">
                        <#else>
                            <span class="file-icon">📄</span>
                        </#if>
                    </div>
                    <div class="attachment-info">
                        <div class="attachment-name">${att.fileName}</div>
                        <div class="attachment-type">${att.fileType!''}</div>
                    </div>
                    <a href="${att.fileUrl}" target="_blank" class="btn btn-download">Открыть файл</a>
                </div>
            </#list>

            <#if !task.attachments?? || task.attachments?size == 0>
                <p class="empty">Пока нет прикреплённых файлов</p>
            </#if>
        </div>
    </div>

</div>


<#if currentUserRole == "ROLE_ADMIN" || currentUserRole == "ROLE_MANAGER">
    <div class="task-actions" style="margin-bottom: 25px;">
        <a href="/tasks/${task.id?c}/edit" class="btn btn-edit">Редактировать задачу</a>
        <a href="/tasks/${task.id?c}/delete"
           class="btn btn-delete"
           onclick="return confirm('Вы уверены, что хотите удалить задачу №${task.id?c}? Это действие нельзя отменить.')">
            Удалить задачу
        </a>
    </div>
</#if>

<script>
    function getCsrfToken() {
        return document.querySelector('meta[name="_csrf"]').getAttribute('content');
    }

    function createSubTask() {
        const name = prompt("Введите название подзадачи:");
        if (!name || name.trim() === '') return;

        fetch('/tasks/${task.id?c}/subtasks', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-CSRF-TOKEN': getCsrfToken()
            },
            body: 'name=' + encodeURIComponent(name.trim())
        })
            .then(response => response.ok ? location.reload() : alert('Ошибка при создании подзадачи'))
            .catch(() => alert('Ошибка соединения'));
    }

    function uploadFile() {
        const fileInput = document.getElementById('fileInput');
        if (!fileInput.files.length) {
            alert('Выберите файл');
            return;
        }

        const formData = new FormData();
        formData.append('file', fileInput.files[0]);

        fetch('/tasks/${task.id?c}/attachments', {
            method: 'POST',
            headers: { 'X-CSRF-TOKEN': getCsrfToken() },
            body: formData
        })
            .then(response => response.ok ? location.reload() : alert('Ошибка загрузки файла'))
            .catch(() => alert('Ошибка соединения'));
    }

    function showCreateSubtaskModal() {
        const modal = document.getElementById('subtaskModal');
        modal.classList.add('show');
        document.getElementById('subtaskName').focus();

        const projectId = "${task.projectId!''}";
        if (projectId) {
            loadProjectUsersForSubtask(projectId);
        }
    }

    function loadProjectUsersForSubtask(projectId) {
        const assigneeSelect = document.getElementById('subtaskAssignee');
        assigneeSelect.innerHTML = '<option value="">— Не назначен —</option>';

        fetch('/users/byProject?projectId=' + projectId)
            .then(response => response.ok ? response.json() : [])
            .then(users => {
                users.forEach(user => {
                    const option = document.createElement('option');
                    option.value = user.id;
                    option.textContent = user.name + ' (' + user.email + ')';
                    assigneeSelect.appendChild(option);
                });
            })
            .catch(() => console.error('Не удалось загрузить участников проекта'));
    }

    function hideModal() {
        const modal = document.getElementById('subtaskModal');
        modal.classList.remove('show');
        document.getElementById('subtaskForm').reset();
    }

    function createSubtaskFromModal() {
        const name = document.getElementById('subtaskName').value.trim();
        if (!name) {
            alert("Название подзадачи обязательно");
            return;
        }

        const assigneeId = document.getElementById('subtaskAssignee').value;
        const deadline = document.getElementById('subtaskDeadline').value;

        const formData = new URLSearchParams();
        formData.append('name', name);
        if (assigneeId) formData.append('assigneeId', assigneeId);
        if (deadline) formData.append('deadline', deadline);

        fetch('/tasks/${task.id?c}/subtasks', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-CSRF-TOKEN': getCsrfToken()
            },
            body: formData
        })
            .then(response => {
                if (response.ok) {
                    hideModal();
                    location.reload();
                } else {
                    alert('Ошибка при создании подзадачи');
                }
            })
            .catch(() => alert('Ошибка соединения'));
    }

    window.onclick = function(event) {
        const modal = document.getElementById('subtaskModal');
        if (event.target === modal) hideModal();
    }
</script>

</body>
</html>