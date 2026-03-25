package ru.itis.soup2.services.project;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.soup2.models.enums.TaskStatus;
import ru.itis.soup2.models.project.Task;
import ru.itis.soup2.repositories.project.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Transactional
    @Override
    public void create(Task task) {
        taskRepository.save(task);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> getTasksByProjectId(Integer projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    @Override
    public List<Task> getTasksBySprintId(Integer sprintId) {
        return taskRepository.findBySprintId(sprintId);
    }

    @Override
    public List<Task> getTasksByAssigneeId(Integer userId) {
        return taskRepository.findByAssigneeId(userId);
    }

    @Override
    public Optional<Task> getTaskById(Integer id) {
        return taskRepository.findById(id);
    }

    @Transactional
    @Override
    public void update(Task task) {
        taskRepository.save(task);
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        if (!task.getComments().isEmpty() || !task.getAttachments().isEmpty()) {
            throw new IllegalStateException("Cannot delete task with comments or attachments");
        }

        taskRepository.delete(task);
    }

    @Override
    public List<Task> findWithFilters(Integer projectId, Integer sprintId, TaskStatus status, Integer userId) {
        return taskRepository.findWithFilters(projectId, sprintId, status, userId);
    }
}