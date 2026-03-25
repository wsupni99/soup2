package ru.itis.soup2.dto;

import java.time.LocalDateTime;

public record TaskLogDto(
        Integer id,
        String action,
        LocalDateTime changedAt,
        Integer userId,
        String userName,
        Integer taskId
) {}