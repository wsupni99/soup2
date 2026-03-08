package ru.itis.soup2.repositories.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.itis.soup2.models.project.TaskLog;

import java.util.List;

public interface TaskLogRepository extends JpaRepository<TaskLog, Long> {
    @Query("SELECT tl FROM TaskLog tl WHERE tl.task.id = :taskId")
    List<TaskLog> findByTaskId(@Param("taskId") Integer taskId);
}
