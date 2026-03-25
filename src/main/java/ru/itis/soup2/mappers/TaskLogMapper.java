package ru.itis.soup2.mappers;

import ru.itis.soup2.dto.TaskLogDto;
import ru.itis.soup2.models.project.TaskLog;
import java.util.List;
import java.util.stream.Collectors;

public final class TaskLogMapper {

    private TaskLogMapper() {}

    public static TaskLogDto toDto(TaskLog log) {
        if (log == null) return null;

        return new TaskLogDto(
                log.getId(),
                log.getAction(),
                log.getChangedAt(),
                log.getUser() != null ? log.getUser().getId() : null,
                log.getUser() != null ? log.getUser().getName() : null,
                log.getTask() != null ? log.getTask().getId() : null
        );
    }

    public static List<TaskLogDto> toDtoList(List<TaskLog> logs) {
        return logs.stream()
                .map(TaskLogMapper::toDto)
                .collect(Collectors.toList());
    }
}