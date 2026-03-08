package ru.itis.soup2.repositories.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.itis.soup2.models.enums.TaskStatus;
import ru.itis.soup2.models.project.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByProjectId(Integer projectId);
    List<Task> findBySprintId(Integer sprintId);
    List<Task> findByParentTaskId(Integer parentTaskId);
    List<Task> findByStatus(TaskStatus status);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.project.projectId = :projectId")
    long countByProjectId(@Param("projectId") Integer projectId);

    @Query("SELECT t FROM Task t WHERE " +
            "(:projectId IS NULL OR t.project.projectId = :projectId) AND " +
            "(:sprintId IS NULL OR t.sprint.sprintId = :sprintId) AND " +
            "(:status IS NULL OR t.status = :status)")
    List<Task> findWithFilters(
            @Param("projectId") Integer projectId,
            @Param("sprintId") Integer sprintId,
            @Param("status") TaskStatus status,
            @Param("userId") Integer userId);
}

