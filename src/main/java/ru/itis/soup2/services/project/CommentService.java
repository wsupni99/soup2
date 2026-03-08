package ru.itis.soup2.services.project;

import org.springframework.transaction.annotation.Transactional;
import ru.itis.soup2.models.project.Comment;

import java.util.List;

public interface CommentService {

    @Transactional
    Comment createComment(Comment comment);
}
