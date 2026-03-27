package ru.itis.soup2.services.project;

import ru.itis.soup2.models.core.User;
import ru.itis.soup2.models.project.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    void create(Task task, Integer assigneeId);

    void update(Task task, Integer assigneeId);

    List<Task> getAllTasks();

    Optional<Task> getTaskById(Integer id);

    void delete(Integer id);

    List<Task> getAllTasksWithFilters(Integer projectId, Integer sprintId, String status,
                                      String priority, Integer assigneeId);

    List<User> getAllUsersForAssignment();
}