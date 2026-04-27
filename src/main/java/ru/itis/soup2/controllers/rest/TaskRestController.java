package ru.itis.soup2.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.itis.soup2.dto.project.TaskDto;
import ru.itis.soup2.mappers.project.TaskMapper;
import ru.itis.soup2.models.enums.TaskPriority;
import ru.itis.soup2.models.enums.TaskStatus;
import ru.itis.soup2.models.project.Task;
import ru.itis.soup2.services.project.TaskService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskRestController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @GetMapping
    public List<TaskDto> getAllTasks(
            @RequestParam(required = false) Integer projectId,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority) {

        List<Task> tasks = taskService.getAllTasksWithFilters(
                projectId, null, status, priority, null, null);
        return taskMapper.toDtoList(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTask(@PathVariable Integer id) {
        return taskService.getTaskById(id)
                .map(task -> ResponseEntity.ok(taskMapper.toDto(task)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        taskService.create(task, taskDto.getAssigneeId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskMapper.toDto(task));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Integer id,
                                              @RequestBody TaskDto taskDto) {
        Task task = taskService.getTaskById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        taskMapper.updateEntity(task, taskDto);
        taskService.update(task, taskDto.getAssigneeId());

        return ResponseEntity.ok(taskMapper.toDto(task));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<Map<String, String>> deleteTask(@PathVariable Integer id) {
        Task task = taskService.getTaskById(id).orElse(null);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }

        taskService.delete(id);
        return ResponseEntity.ok(Map.of("message",
                "Задача №" + id + " успешно удалена"));
    }
}