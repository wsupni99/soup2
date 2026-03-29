package ru.itis.soup2.services.project;

import org.springframework.transaction.annotation.Transactional;
import ru.itis.soup2.models.core.User;
import ru.itis.soup2.models.project.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    List<Task> getAllTasks();

    Optional<Task> getTaskById(Integer id);

    List<User> getAllUsersForAssignment();

    List<Task> getAllTasksWithFilters(Integer projectId, Integer sprintId, String status,
                                      String priority, Integer assigneeId, String search);

    @Transactional
    void create(Task task, Integer assigneeId);

    @Transactional
    void update(Task task, Integer assigneeId);

    @Transactional
    void delete(Integer id);
}
