package ru.itis.soup2.dto;

import ru.itis.soup2.models.enums.TaskPriority;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TaskDto(
        Integer id,
        String name,
        String description,
        TaskPriority priority,
        String status,
        LocalDate deadline,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,

        Integer projectId,
        String projectName,
        Integer sprintId,
        String sprintName,
        Integer parentTaskId,
        String parentTaskName,

        // Один исполнитель вместо списка
        Integer assigneeId,
        String assigneeName
) {

    // Конструктор для формы создания/редактирования
    public TaskDto(String name, String description, TaskPriority priority, String status,
                   LocalDate deadline, Integer projectId, Integer sprintId, Integer assigneeId) {
        this(null, name, description, priority, status, deadline, null, null,
                projectId, null, sprintId, null, null, null, assigneeId, null);
    }
}