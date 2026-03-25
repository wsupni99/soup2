package ru.itis.soup2.mappers;

import ru.itis.soup2.dto.CommentDto;
import ru.itis.soup2.models.project.Comment;
import java.util.List;
import java.util.stream.Collectors;

public final class CommentMapper {

    private CommentMapper() {}

    public static CommentDto toDto(Comment comment) {
        if (comment == null) return null;
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getCreatedAt(),
                comment.getUser() != null ? comment.getUser().getId() : null,
                comment.getUser() != null ? comment.getUser().getName() : null,
                comment.getTask() != null ? comment.getTask().getId() : null
        );
    }

    public static List<CommentDto> toDtoList(List<Comment> comments) {
        return comments.stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());
    }
}