// ru.itis.soup2.dto.project.CommentDto
package ru.itis.soup2.dto.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.soup2.models.project.Comment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Integer id;
    private String text;
    private LocalDateTime createdAt;
    private Integer userId;
    private String userName;
    private Integer taskId;

    public String getFormattedCreatedAt() {
        if (createdAt == null) return "";
        return createdAt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
    }

    public static CommentDto from(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .createdAt(comment.getCreatedAt())
                .userId(comment.getUser() != null ? comment.getUser().getId() : null)
                .userName(comment.getUser() != null ? comment.getUser().getName() : null)
                .taskId(comment.getTask().getId())
                .build();
    }
}