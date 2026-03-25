package ru.itis.soup2.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.soup2.dto.TaskDto;
import ru.itis.soup2.models.enums.TaskStatus;
import ru.itis.soup2.models.project.Task;
import ru.itis.soup2.models.core.User;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskMapper {

    public TaskDto toDto(Task task) {
        if (task == null) return null;

        List<Integer> assigneeIds = task.getAssignees() == null ? List.of() :
                task.getAssignees().stream().map(User::getId).collect(Collectors.toList());

        List<String> assigneeNames = task.getAssignees() == null ? List.of() :
                task.getAssignees().stream().map(User::getName).collect(Collectors.toList());

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
                assigneeIds,
                assigneeNames,
                task.getParentTask() != null ? task.getParentTask().getId() : null,
                task.getParentTask() != null ? task.getParentTask().getName() : null
        );
    }

    public List<TaskDto> toDtoList(List<Task> tasks) {
        return tasks.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
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
        // project, sprint, parentTask,createdAt и updatedAt устанавливаются в сервисе
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
        // updatedAt устанавливается в сервисе
    }
}