package ru.itis.soup2.dto.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.soup2.models.enums.TaskPriority;
import ru.itis.soup2.models.project.Task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    private Integer id;
    private String name;
    private String description;
    private TaskPriority priority;
    private String status;
    private LocalDate deadline;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Integer projectId;
    private String projectName;
    private Integer sprintId;
    private String sprintName;
    private Integer parentTaskId;
    private String parentTaskName;

    private Integer assigneeId;
    private String assigneeName;

    public static TaskDto from(Task task) {
        if (task == null) return null;

        return TaskDto.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .priority(task.getPriority())
                .status(task.getStatus() != null ? task.getStatus().name() : null)
                .deadline(task.getDeadline())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .projectId(task.getProject() != null ? task.getProject().getId() : null)
                .projectName(task.getProject() != null ? task.getProject().getName() : null)
                .sprintId(task.getSprint() != null ? task.getSprint().getId() : null)
                .sprintName(task.getSprint() != null ? task.getSprint().getName() : null)
                .parentTaskId(task.getParentTask() != null ? task.getParentTask().getId() : null)
                .parentTaskName(task.getParentTask() != null ? task.getParentTask().getName() : null)
                .assigneeId(task.getAssignee() != null ? task.getAssignee().getId() : null)
                .assigneeName(task.getAssignee() != null ? task.getAssignee().getName() : null)
                .build();
    }

    public static List<TaskDto> from(List<Task> tasks) {
        return tasks.stream()
                .map(TaskDto::from)
                .toList();
    }

    public TaskDto(String name, String description, TaskPriority priority, String status,
                   LocalDate deadline, Integer projectId, Integer sprintId, Integer assigneeId) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.deadline = deadline;
        this.projectId = projectId;
        this.sprintId = sprintId;
        this.assigneeId = assigneeId;
    }
}