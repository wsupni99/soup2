package ru.itis.soup2.mappers.project;

import org.springframework.stereotype.Component;
import ru.itis.soup2.dto.project.TaskDto;
import ru.itis.soup2.models.enums.TaskStatus;
import ru.itis.soup2.models.project.Project;
import ru.itis.soup2.models.project.Sprint;
import ru.itis.soup2.models.project.Task;

import java.util.List;

@Component
public class TaskMapper {

    public TaskDto toDto(Task task) {
        return TaskDto.from(task);
    }

    public List<TaskDto> toDtoList(List<Task> tasks) {
        return TaskDto.from(tasks);
    }

    public Task toEntity(TaskDto dto) {
        if (dto == null) return null;

        Task task = new Task();
        task.setName(dto.getName());
        task.setDescription(dto.getDescription());
        task.setPriority(dto.getPriority());

        if (dto.getStatus() != null && !dto.getStatus().trim().isEmpty()) {
            task.setStatus(TaskStatus.valueOf(dto.getStatus().trim().toUpperCase()));
        }

        task.setDeadline(dto.getDeadline());

        // === Главное исправление ===
        if (dto.getProjectId() != null) {
            Project project = new Project();
            project.setId(dto.getProjectId());
            task.setProject(project);
        }

        if (dto.getSprintId() != null) {
            Sprint sprint = new Sprint();
            sprint.setId(dto.getSprintId());
            task.setSprint(sprint);
        }

        return task;
    }

    public void updateEntity(Task task, TaskDto dto) {
        if (dto == null || task == null) return;

        task.setName(dto.getName());
        task.setDescription(dto.getDescription());
        task.setPriority(dto.getPriority());

        if (dto.getStatus() != null && !dto.getStatus().trim().isEmpty()) {
            task.setStatus(TaskStatus.valueOf(dto.getStatus().trim().toUpperCase()));
        }

        task.setDeadline(dto.getDeadline());

        // Обновление связей
        if (dto.getProjectId() != null) {
            Project project = new Project();
            project.setId(dto.getProjectId());
            task.setProject(project);
        } else {
            task.setProject(null);
        }

        if (dto.getSprintId() != null) {
            Sprint sprint = new Sprint();
            sprint.setId(dto.getSprintId());
            task.setSprint(sprint);
        } else {
            task.setSprint(null);
        }
    }
}