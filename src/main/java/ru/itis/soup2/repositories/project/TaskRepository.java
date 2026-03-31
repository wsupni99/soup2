package ru.itis.soup2.repositories.project;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.itis.soup2.models.enums.TaskPriority;
import ru.itis.soup2.models.enums.TaskStatus;
import ru.itis.soup2.models.project.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    @EntityGraph(attributePaths = {"project", "sprint", "parentTask", "assignee"})
    @Query("SELECT t FROM Task t")
    List<Task> findAllWithDetails();

    @EntityGraph(attributePaths = {"project", "sprint", "parentTask", "assignee"})
    @Query("SELECT t FROM Task t WHERE t.id = :id")
    Optional<Task> findWithDetailsById(@Param("id") Integer id);

    @EntityGraph(attributePaths = {"project", "sprint", "parentTask", "assignee"})
    @Query("""
    SELECT t FROM Task t
    WHERE (:projectId IS NULL OR t.project.id = :projectId)
      AND (:sprintId IS NULL OR t.sprint.id = :sprintId)
      AND (:status IS NULL OR :status = '' OR t.status = :statusEnum)
      AND (:priority IS NULL OR :priority = '' OR t.priority = :priorityEnum)
      AND (:assigneeId IS NULL OR t.assignee.id = :assigneeId)
      AND (:search IS NULL OR :search = ''
           OR LOWER(t.name) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(COALESCE(t.description, '')) LIKE LOWER(CONCAT('%', :search, '%')))
    ORDER BY t.id DESC
    """)
    List<Task> findTasksWithFilters(
            @Param("projectId") Integer projectId,
            @Param("sprintId") Integer sprintId,
            @Param("status") String status,
            @Param("statusEnum") TaskStatus statusEnum,
            @Param("priority") String priority,
            @Param("priorityEnum") TaskPriority priorityEnum,
            @Param("assigneeId") Integer assigneeId,
            @Param("search") String search
    );
}