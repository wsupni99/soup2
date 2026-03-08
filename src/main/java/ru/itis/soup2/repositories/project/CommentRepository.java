package ru.itis.soup2.repositories.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.itis.soup2.models.project.Comment;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query("SELECT c FROM Comment c WHERE c.task.taskId = :taskId")
    List<Comment> findByTaskId(@Param("taskId") Integer taskId);

    @Query("SELECT c FROM Comment c WHERE c.user.userId = :userId")
    List<Comment> findByUserId(@Param("userId") Integer userId);

    @Query("SELECT c FROM Comment c WHERE c.task.taskId = :taskId ORDER BY c.createdAt DESC")
    List<Comment> findByTaskIdOrderByCreatedAtDesc(@Param("taskId") Integer taskId);

    @Query("SELECT c FROM Comment c WHERE c.task.taskId = :taskId AND c.createdAt BETWEEN :from AND :to")
    List<Comment> findByTaskIdAndCreatedAtBetween(
            @Param("taskId") Integer taskId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}