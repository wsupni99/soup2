package ru.itis.soup2.repositories.project;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    List<Task> findByAssigneeId(Integer assigneeId);
}