package ru.itis.soup2.services.project;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.soup2.models.project.Attachment;
import ru.itis.soup2.repositories.project.AttachmentRepository;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttachmentServiceImpl implements AttachmentService {
    private final AttachmentRepository attachmentRepository;

    @Transactional
    @Override
    public Attachment create(Attachment attachment) {
        try {
            return attachmentRepository.save(attachment);
        } catch (Exception e) {
            log.error("Ошибка при сохранении вложения. Причина: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Optional<Attachment> findById(Integer id) {
        try {
            return attachmentRepository.findById(id);
        } catch (Exception e) {
            log.error("Ошибка при поиске вложения с id: {}. Причина: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<Attachment> findByTaskId(Integer taskId) {
        try {
            return attachmentRepository.findByTaskId(taskId);
        } catch (Exception e) {
            log.error("Ошибка при поиске вложений по задаче с id: {}. Причина: {}", taskId, e.getMessage(), e);
            throw e;
        }
    }
}