package ru.itis.soup2.repositories.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.itis.soup2.models.enums.TaskStatus;
import ru.itis.soup2.models.project.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    @Query("SELECT t FROM Task t JOIN t.assignees u WHERE u.id = :userId")
    List<Task> findByAssigneeId(@Param("userId") Integer userId);

    List<Task> findByProjectId(Integer projectId);

    List<Task> findBySprintId(Integer sprintId);

    List<Task> findByStatus(TaskStatus status);

    @Query("SELECT t FROM Task t WHERE " +
            "(:projectId IS NULL OR t.project.id = :projectId) AND " +
            "(:sprintId IS NULL OR t.sprint.id = :sprintId) AND " +
            "(:status IS NULL OR t.status = :status) AND " +
            "(:userId IS NULL OR EXISTS (SELECT 1 FROM t.assignees u WHERE u.id = :userId))")
    List<Task> findWithFilters(
            @Param("projectId") Integer projectId,
            @Param("sprintId") Integer sprintId,
            @Param("status") TaskStatus status,
            @Param("userId") Integer userId);
}