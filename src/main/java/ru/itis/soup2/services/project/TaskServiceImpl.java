package ru.itis.soup2.services.project;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.soup2.models.core.User;
import ru.itis.soup2.models.project.Task;
import ru.itis.soup2.repositories.core.UserRepository;
import ru.itis.soup2.repositories.project.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

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
    public List<Task> getAllTasksWithFilters(Integer projectId, Integer sprintId, String status,
                                             String priority, Integer assigneeId) {
        List<Task> tasks = taskRepository.findAllWithDetails();

        return tasks.stream()
                .filter(task -> projectId == null ||
                        (task.getProject() != null && task.getProject().getId().equals(projectId)))
                .filter(task -> sprintId == null ||
                        (task.getSprint() != null && task.getSprint().getId().equals(sprintId)))
                .filter(task -> status == null || status.isEmpty() ||
                        (task.getStatus() != null && task.getStatus().name().equals(status)))
                .filter(task -> priority == null || priority.isEmpty() ||
                        (task.getPriority() != null && task.getPriority().name().equals(priority)))
                .filter(task -> assigneeId == null ||
                        (task.getAssignee() != null && task.getAssignee().getId().equals(assigneeId)))
                .toList();
    }
}