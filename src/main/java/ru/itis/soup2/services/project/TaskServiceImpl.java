package ru.itis.soup2.services.project;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.soup2.models.core.User;
import ru.itis.soup2.models.enums.TaskPriority;
import ru.itis.soup2.models.enums.TaskStatus;
import ru.itis.soup2.models.project.Attachment;
import ru.itis.soup2.models.project.ProjectMember;
import ru.itis.soup2.models.project.Task;
import ru.itis.soup2.repositories.core.UserRepository;
import ru.itis.soup2.repositories.project.ProjectMemberRepository;
import ru.itis.soup2.repositories.project.TaskRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final AttachmentService attachmentService;
    private final ProjectMemberRepository projectMemberRepository;

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
        if (assigneeId != null) {
            userRepository.findById(assigneeId)
                    .ifPresent(task::setAssignee);
        }
        taskRepository.save(task);
    }

    @Transactional
    @Override
    public void update(Task task, Integer assigneeId) {
        if (assigneeId != null) {
            userRepository.findById(assigneeId)
                    .ifPresent(task::setAssignee);
        } else {
            task.setAssignee(null);
        }
        taskRepository.save(task);
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        Task task = taskRepository.findWithDetailsById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        if (!task.getComments().isEmpty() || !task.getAttachments().isEmpty()) {
            throw new IllegalStateException("Нельзя удалить задачу, у которой есть комментарии или прикреплённые файлы");
        }

        taskRepository.delete(task);
    }

    @Override
    @Transactional
    public Task createSubTask(Integer parentTaskId, Task subTask, Integer assigneeId, LocalDate deadline) {
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

        return taskRepository.save(subTask);
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

            return task;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при сохранении файла: " + e.getMessage(), e);
        }
    }

    @Override
    public List<User> getUsersByProjectId(Integer projectId) {
        if (projectId == null) {
            return List.of();
        }
        return projectMemberRepository.findByProjectId(projectId).stream()
                .map(ProjectMember::getUser)
                .toList();
    }
}