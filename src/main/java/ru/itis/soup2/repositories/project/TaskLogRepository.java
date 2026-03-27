package ru.itis.soup2.repositories.project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.itis.soup2.models.project.TaskLog;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskLogRepository extends JpaRepository<TaskLog, Integer> {

    @Query("SELECT tl FROM TaskLog tl ORDER BY tl.changedAt DESC")
    Page<TaskLog> findAllOrdered(Pageable pageable);

    @Query("SELECT tl FROM TaskLog tl WHERE (:userId IS NULL OR tl.user.id = :userId) ORDER BY tl.changedAt DESC")
    Page<TaskLog> findByUserIdOptional(@Param("userId") Integer userId, Pageable pageable);
}