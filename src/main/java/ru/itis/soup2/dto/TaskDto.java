package ru.itis.soup2.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record TaskDto(
        Integer id,
        String name,
        String description,
        ru.itis.soup2.models.enums.TaskPriority priority,
        String status,
        LocalDate deadline,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Integer projectId,
        String projectName,
        Integer sprintId,
        String sprintName,
        List<Integer> assigneeIds,
        List<String> assigneeNames,
        Integer parentTaskId,
        String parentTaskName
) {}