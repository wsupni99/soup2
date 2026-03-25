package ru.itis.soup2.dto;

import java.time.LocalDateTime;

public record CommentDto(
        Integer id,
        String text,
        LocalDateTime createdAt,
        Integer userId,
        String userName,
        Integer taskId
) {}