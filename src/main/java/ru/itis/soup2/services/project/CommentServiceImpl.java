// ru.itis.soup2.services.project.CommentServiceImpl
package ru.itis.soup2.services.project;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.soup2.models.core.User;
import ru.itis.soup2.models.project.Comment;
import ru.itis.soup2.models.project.Task;
import ru.itis.soup2.repositories.core.UserRepository;
import ru.itis.soup2.repositories.project.CommentRepository;
import ru.itis.soup2.repositories.project.TaskRepository;
import ru.itis.soup2.services.mail.MailService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final MailService mailService;

    @Transactional
    @Override
    public Comment createComment(Comment comment) {
        try {
            comment.setCreatedAt(LocalDateTime.now());
            Comment saved = commentRepository.save(comment);

            // Уведомление исполнителю задачи
            Task task = taskRepository.findWithDetailsById(comment.getTask().getId())
                    .orElseThrow(() -> new RuntimeException("Task not found"));
            if (task.getAssignee() != null && task.getAssignee().getEmail() != null) {
                String assigneeEmail = task.getAssignee().getEmail();
                Map<String, Object> model = Map.of(
                        "taskName", task.getName(),
                        "action", "Новый комментарий: " + comment.getText(),
                        "taskStatus", task.getStatus() != null ? task.getStatus().toString() : "—",
                        "taskAssignee", task.getAssignee().getName(),
                        "taskLink", "http://localhost:8080/tasks/" + task.getId()
                );
                mailService.sendTaskNotification(assigneeEmail, "Новый комментарий в задаче", "mail/task_notification.ftlh", model);
            }

            if (task.getProject() != null && task.getProject().getManager() != null) {
                String managerEmail = task.getProject().getManager().getEmail();
                // Не отправлять дважды, если менеджер - исполнитель
                if (task.getAssignee() == null || !managerEmail.equals(task.getAssignee().getEmail())) {
                    Map<String, Object> model = Map.of(
                            "taskName", task.getName(),
                            "action", "Новый комментарий от " + comment.getUser().getName() + ": " + comment.getText(),
                            "taskStatus", task.getStatus() != null ? task.getStatus().toString() : "—",
                            "taskAssignee", task.getAssignee() != null ? task.getAssignee().getName() : "—",
                            "taskLink", "http://localhost:8080/tasks/" + task.getId()
                    );
                    mailService.sendTaskNotification(managerEmail, "Новый комментарий в задаче проекта", "mail/task_notification.ftlh", model);
                }
            }

            return saved;
        } catch (Exception e) {
            log.error("Ошибка при создании комментария. Причина: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public Comment createComment(Integer taskId, Integer userId, String text) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        Comment comment = new Comment();
        comment.setTask(task);
        comment.setUser(user);
        comment.setText(text);

        return createComment(comment);
    }

    @Override
    public List<Comment> findByTaskId(Integer taskId) {
        try {
            return commentRepository.findByTaskId(taskId);
        } catch (Exception e) {
            log.error("Ошибка при поиске комментариев по задаче с id: {}. Причина: {}", taskId, e.getMessage(), e);
            throw e;
        }
    }
}