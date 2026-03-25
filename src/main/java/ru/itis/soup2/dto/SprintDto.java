package ru.itis.soup2.dto;

import java.time.LocalDate;

public record SprintDto(
        Integer id,
        String name,
        LocalDate startDate,
        LocalDate endDate,
        Integer projectId,
        String projectName
) {}