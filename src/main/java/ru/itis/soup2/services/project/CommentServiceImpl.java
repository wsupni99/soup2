package ru.itis.soup2.services.project;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.soup2.models.project.Comment;
import ru.itis.soup2.repositories.project.CommentRepository;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public Comment createComment(Comment comment) {
        comment.setCreatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    public List<Comment> findByTaskId(Integer taskId) {
        return commentRepository.findByTaskId(taskId);
    }
}
