package ru.itis.soup2.services.project;

import org.springframework.transaction.annotation.Transactional;
import ru.itis.soup2.models.project.Attachment;

import java.util.List;
import java.util.Optional;

public interface AttachmentService {
    @Transactional
    Attachment create(Attachment attachment);

    Optional<Attachment> findById(Integer id);

    List<Attachment> findByTaskId(Integer taskId);
}
