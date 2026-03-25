package ru.itis.soup2.dto;

public record WorkloadSummaryDto(
        Integer id,
        Integer userId,
        String userName,
        Integer projectId,
        String projectName,
        Integer sprintId,
        String sprintName,
        Integer openTasksCount,
        Integer closedTasksCount
) {}