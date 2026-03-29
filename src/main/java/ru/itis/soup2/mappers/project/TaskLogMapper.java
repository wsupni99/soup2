package ru.itis.soup2.mappers.project;

import org.springframework.stereotype.Component;
import ru.itis.soup2.dto.project.TaskLogDto;
import ru.itis.soup2.models.project.TaskLog;

@Component
public class TaskLogMapper {

    public TaskLogDto toDto(TaskLog log) {
        return new TaskLogDto(
                log.getId(),
                log.getAction(),
                log.getChangedAt(),
                log.getUser().getId(),
                log.getUser().getName(),
                log.getTask().getId()
        );
    }
}