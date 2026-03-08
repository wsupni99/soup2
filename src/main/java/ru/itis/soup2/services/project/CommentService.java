package ru.itis.soup2.services.project;

import org.springframework.transaction.annotation.Transactional;
import ru.itis.soup2.models.project.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getCommentsByTaskId(Integer taskId);

    @Transactional
    Comment createComment(Comment comment);
}
