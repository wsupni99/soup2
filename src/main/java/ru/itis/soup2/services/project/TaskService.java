package ru.itis.soup2.services.project;

import ru.itis.soup2.models.enums.TaskStatus;
import ru.itis.soup2.models.project.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    void create(Task task);

    List<Task> getAllTasks();

    List<Task> getTasksByProjectId(Integer projectId);

    List<Task> getTasksBySprintId(Integer sprintId);

    List<Task> getTasksByAssigneeId(Integer userId);

    Optional<Task> getTaskById(Integer id);

    void update(Task task);

    void delete(Integer id);

    // Для фильтров на странице (если захочешь)
    List<Task> findWithFilters(Integer projectId, Integer sprintId, TaskStatus status, Integer userId);
}