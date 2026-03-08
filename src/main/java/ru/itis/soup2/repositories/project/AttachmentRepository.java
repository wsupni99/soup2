package ru.itis.soup2.repositories.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.itis.soup2.models.project.Attachment;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {
    @Query("SELECT a FROM Attachment a WHERE a.task.id = :taskId")
    List<Attachment> findByTaskId(@Param("taskId") Integer taskId);
}
