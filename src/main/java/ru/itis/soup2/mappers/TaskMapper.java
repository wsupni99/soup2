package ru.itis.soup2.mappers;

import org.springframework.stereotype.Component;
import ru.itis.soup2.dto.TaskDto;
import ru.itis.soup2.models.enums.TaskStatus;
import ru.itis.soup2.models.project.Task;
import ru.itis.soup2.models.core.User;

import java.util.List;

@Component
public class TaskMapper {

    public TaskDto toDto(Task task) {
        if (task == null) return null;

        return new TaskDto(
                task.getId(),
                task.getName(),
                task.getDescription(),
                task.getPriority(),
                task.getStatus() != null ? task.getStatus().name() : null,
                task.getDeadline(),
                task.getCreatedAt(),
                task.getUpdatedAt(),

                task.getProject() != null ? task.getProject().getId() : null,
                task.getProject() != null ? task.getProject().getName() : null,
                task.getSprint() != null ? task.getSprint().getId() : null,
                task.getSprint() != null ? task.getSprint().getName() : null,
                task.getParentTask() != null ? task.getParentTask().getId() : null,
                task.getParentTask() != null ? task.getParentTask().getName() : null,

                task.getAssignee() != null ? task.getAssignee().getId() : null,
                task.getAssignee() != null ? task.getAssignee().getName() : null
        );
    }

    public List<TaskDto> toDtoList(List<Task> tasks) {
        return tasks.stream().map(this::toDto).toList();
    }

    public Task toEntity(TaskDto dto) {
        if (dto == null) return null;

        Task task = new Task();
        task.setName(dto.name());
        task.setDescription(dto.description());
        task.setPriority(dto.priority());
        if (dto.status() != null) {
            task.setStatus(TaskStatus.valueOf(dto.status()));
        }
        task.setDeadline(dto.deadline());
        return task;
    }

    public void updateEntity(Task task, TaskDto dto) {
        if (dto == null || task == null) return;

        task.setName(dto.name());
        task.setDescription(dto.description());
        task.setPriority(dto.priority());
        if (dto.status() != null) {
            task.setStatus(TaskStatus.valueOf(dto.status()));
        }
        task.setDeadline(dto.deadline());
    }
}