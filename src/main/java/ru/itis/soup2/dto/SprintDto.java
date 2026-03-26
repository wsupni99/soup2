package ru.itis.soup2.dto;

import java.time.LocalDate;
import java.util.List;

public record SprintDto(
        Integer id,
        String name,
        LocalDate startDate,
        LocalDate endDate,
        Integer projectId,
        String projectName,
        List<TaskDto> tasks,
        Integer taskCount
) {
    public SprintDto(Integer id, String name, LocalDate startDate, LocalDate endDate,
                     Integer projectId, String projectName) {
        this(id, name, startDate, endDate, projectId, projectName, List.of(), 0);
    }

    // Конструктор с задачами (для отображения)
    public SprintDto(Integer id, String name, LocalDate startDate, LocalDate endDate,
                     Integer projectId, String projectName,
                     List<TaskDto> tasks) {
        this(id, name, startDate, endDate, projectId, projectName,
                tasks != null ? tasks : List.of(),
                tasks != null ? tasks.size() : (int) 0L);
    }
}