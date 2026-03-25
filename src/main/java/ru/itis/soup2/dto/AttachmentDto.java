package ru.itis.soup2.dto;

public record AttachmentDto(
        Integer id,
        String fileName,
        String fileUrl,
        String fileType,
        Integer taskId
) {}