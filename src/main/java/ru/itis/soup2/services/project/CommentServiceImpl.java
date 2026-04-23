package ru.itis.soup2.services.project;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.soup2.models.project.Comment;
import ru.itis.soup2.repositories.project.CommentRepository;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public Comment createComment(Comment comment) {
        try {
            comment.setCreatedAt(LocalDateTime.now());
            return commentRepository.save(comment);
        } catch (Exception e) {
            log.error("Ошибка при создании комментария", e);
            throw e;
        }
    }

    @Override
    public List<Comment> findByTaskId(Integer taskId) {
        try {
            return commentRepository.findByTaskId(taskId);
        } catch (Exception e) {
            log.error("Ошибка при поиске комментариев по задаче с id: {}", taskId, e);
            throw e;
        }
    }
}