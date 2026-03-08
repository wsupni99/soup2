package ru.itis.soup2.services.project;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.soup2.models.project.Attachment;
import ru.itis.soup2.repositories.project.AttachmentRepository;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {
    private final AttachmentRepository attachmentsRepository;

    @Transactional
    @Override
    public Attachment create(Attachment attachment) {
        return attachmentsRepository.save(attachment);
    }

    @Override
    public Optional<Attachment> findById(Integer id) {
        return attachmentsRepository.findById(id);
    }

    @Override
    public List<Attachment> findByTaskId(Integer taskId) {
        return attachmentsRepository.findByTaskId(taskId);
    }
}

