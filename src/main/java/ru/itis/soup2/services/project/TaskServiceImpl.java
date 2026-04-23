package ru.itis.soup2.services.project;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.soup2.models.core.User;
import ru.itis.soup2.models.enums.TaskPriority;
import ru.itis.soup2.models.enums.TaskStatus;
import ru.itis.soup2.models.project.Attachment;
import ru.itis.soup2.models.project.Project;
import ru.itis.soup2.models.project.ProjectMember;
import ru.itis.soup2.models.project.Task;
import ru.itis.soup2.repositories.core.UserRepository;
import ru.itis.soup2.repositories.project.ProjectMemberRepository;
import ru.itis.soup2.repositories.project.TaskRepository;
import ru.itis.soup2.services.mail.MailService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final AttachmentService attachmentService;
    private final ProjectMemberRepository projectMemberRepository;
    private final MailService mailService;

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAllWithDetails();
    }

    @Override
    public Optional<Task> getTaskById(Integer id) {
        return taskRepository.findWithDetailsById(id);
    }

    @Override
    public List<User> getAllUsersForAssignment() {
        return userRepository.findAll();
    }

    @Override
    public List<Task> getAllTasksWithFilters(Integer projectId, Integer sprintId, String status,
                                             String priority, Integer assigneeId, String search) {

        TaskStatus statusEnum = null;
        if (status != null && !status.trim().isEmpty()) {
            try {
                statusEnum = TaskStatus.valueOf(status.trim().toUpperCase());
            } catch (IllegalArgumentException ignored) {}
        }

        TaskPriority priorityEnum = null;
        if (priority != null && !priority.trim().isEmpty()) {
            try {
                priorityEnum = TaskPriority.valueOf(priority.trim().toUpperCase());
            } catch (IllegalArgumentException ignored) {}
        }

        String cleanSearch = (search != null && !search.trim().isEmpty())
                ? search.trim()
                : null;

        return taskRepository.findTasksWithFilters(
                projectId, sprintId, status, statusEnum,
                priority, priorityEnum, assigneeId, cleanSearch
        );
    }

    @Transactional
    @Override
    public void create(Task task, Integer assigneeId) {
        try {
            if (assigneeId != null) {
                userRepository.findById(assigneeId)
                        .ifPresent(task::setAssignee);
            }
            taskRepository.save(task);

            // Уведомление менеджеру проекта (если есть)
            if (task.getProject() != null && task.getProject().getManager() != null) {
                String managerEmail = task.getProject().getManager().getEmail();
                if (managerEmail != null && !managerEmail.isBlank()) {
                    Map<String, Object> model = Map.of(
                            "taskName", task.getName(),
                            "action", "Создана новая задача",
                            "taskStatus", task.getStatus() != null ? task.getStatus() : "—",
                            "taskAssignee", task.getAssignee() != null ? task.getAssignee().getName() : "—",
                            "taskLink", "http://localhost:8080/tasks/" + task.getId()
                    );
                    mailService.sendTaskNotification(managerEmail, "Новая задача в проекте", "mail/task_notification.ftlh", model);
                }
            }
        } catch (Exception e) {
            log.error("Ошибка при создании задачи: {}. Причина: {}", task.getName(), e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    @Override
    public void update(Task task, Integer assigneeId) {
        try {
            if (assigneeId != null) {
                userRepository.findById(assigneeId)
                        .ifPresent(task::setAssignee);
            } else {
                task.setAssignee(null);
            }
            taskRepository.save(task);

            Map<String, Object> model = buildNotificationModel(task, "Задача обновлена");

            // Уведомление менеджеру проекта
            if (task.getProject() != null && task.getProject().getManager() != null) {
                String managerEmail = task.getProject().getManager().getEmail();
                if (managerEmail != null && !managerEmail.isBlank()) {
                    mailService.sendTaskNotification(managerEmail, "Обновление задачи", "mail/task_notification.ftlh", model);
                }
            }

            // Уведомление назначенному исполнителю
            if (task.getAssignee() != null) {
                String assigneeEmail = task.getAssignee().getEmail();

                boolean isNotManager = task.getProject().getManager() == null ||
                        !assigneeEmail.equals(task.getProject().getManager().getEmail());

                if (assigneeEmail != null && !assigneeEmail.isBlank() && isNotManager) {
                    mailService.sendTaskNotification(assigneeEmail, "Вам назначена/обновлена задача", "mail/task_notification.ftlh", model);
                }
            }

        } catch (Exception e) {
            log.error("Ошибка при обновлении задачи с id: {}. Причина: {}", task.getId(), e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        try {
            Task task = taskRepository.findWithDetailsById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Task not found"));

            if (!task.getComments().isEmpty() || !task.getAttachments().isEmpty()) {
                throw new IllegalStateException("Нельзя удалить задачу, у которой есть комментарии или прикреплённые файлы");
            }

            taskRepository.delete(task);
        } catch (Exception e) {
            log.error("Ошибка при удалении задачи с id: {}. Причина: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public Task createSubTask(Integer parentTaskId, Task subTask, Integer assigneeId, LocalDate deadline) {
        try {
            Task parent = taskRepository.findWithDetailsById(parentTaskId)
                    .orElseThrow(() -> new EntityNotFoundException("Parent task not found"));

            if (assigneeId != null) {
                userRepository.findById(assigneeId).ifPresent(subTask::setAssignee);
            }

            subTask.setParentTask(parent);
            subTask.setProject(parent.getProject());
            subTask.setSprint(parent.getSprint());
            subTask.setStatus(TaskStatus.TODO);
            subTask.setCreatedAt(LocalDateTime.now());
            subTask.setUpdatedAt(LocalDateTime.now());

            if (deadline != null) {
                subTask.setDeadline(deadline);
            }

            Task saved = taskRepository.save(subTask);

            // Уведомление родительскому исполнителю (если есть)
            if (parent.getAssignee() != null && parent.getAssignee().getEmail() != null) {
                String parentAssigneeEmail = parent.getAssignee().getEmail();
                Map<String, Object> model = buildNotificationModel(saved, "Создана подзадача");
                mailService.sendTaskNotification(parentAssigneeEmail, "Новая подзадача", "mail/task_notification.ftlh", model);
            }

            return saved;
        } catch (Exception e) {
            log.error("Ошибка при создании подзадачи для задачи с id: {}. Причина: {}", parentTaskId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public Task addAttachment(Integer taskId, MultipartFile file) {
        Task task = taskRepository.findWithDetailsById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        if (file.isEmpty()) {
            throw new IllegalArgumentException("Файл пустой");
        }

        try {
            java.nio.file.Path uploadDir = java.nio.file.Paths.get("uploads").toAbsolutePath().normalize();

            if (!java.nio.file.Files.exists(uploadDir)) {
                java.nio.file.Files.createDirectories(uploadDir);
            }

            String originalName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "file";
            String extension = "";
            int dotIndex = originalName.lastIndexOf('.');
            if (dotIndex > 0) {
                extension = originalName.substring(dotIndex);
            }

            String uniqueFilename = java.util.UUID.randomUUID() + extension;
            java.nio.file.Path filePath = uploadDir.resolve(uniqueFilename);

            file.transferTo(filePath.toFile());

            Attachment attachment = Attachment.builder()
                    .task(task)
                    .fileName(originalName)
                    .fileUrl("/uploads/" + uniqueFilename)
                    .fileType(file.getContentType())
                    .build();

            attachmentService.create(attachment);

            // Уведомление назначенному исполнителю
            if (task.getAssignee() != null && task.getAssignee().getEmail() != null) {
                String assigneeEmail = task.getAssignee().getEmail();
                Map<String, Object> model = buildNotificationModel(task, "Добавлен новый файл: " + originalName);
                mailService.sendTaskNotification(assigneeEmail, "Новый файл в задаче", "mail/task_notification.ftlh", model);
            }

            return task;
        } catch (Exception e) {
            log.error("Ошибка при добавлении вложения к задаче с id: {}. Причина: {}", taskId, e.getMessage(), e);
            throw new RuntimeException("Ошибка при сохранении файла: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Task> getSubTasks(Integer parentTaskId) {
        return taskRepository.findByParentTaskId(parentTaskId);
    }

    @Override
    public List<User> getUsersByProjectId(Integer projectId) {
        if (projectId == null) {
            return List.of();
        }
        return projectMemberRepository.findAllByProjectId(projectId).stream()
                .map(ProjectMember::getUser)
                .toList();
    }

    private Map<String, Object> buildNotificationModel(Task task, String action) {
        Map<String, Object> model = new HashMap<>();
        model.put("taskName", task.getName());
        model.put("action", action);
        model.put("taskStatus", task.getStatus() != null ? task.getStatus().toString() : "—");
        model.put("taskAssignee", task.getAssignee() != null ? task.getAssignee().getName() : "—");
        model.put("taskLink", "http://localhost:8080/tasks/" + task.getId());
        return model;
    }
}