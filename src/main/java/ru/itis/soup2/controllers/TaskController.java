package ru.itis.soup2.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itis.soup2.dto.TaskDto;
import ru.itis.soup2.mappers.TaskMapper;
import ru.itis.soup2.models.enums.TaskPriority;
import ru.itis.soup2.models.project.Task;
import ru.itis.soup2.services.project.ProjectService;
import ru.itis.soup2.services.project.SprintService;
import ru.itis.soup2.services.project.TaskService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final ProjectService projectService;
    private final SprintService sprintService;
    private final TaskMapper taskMapper;

    @GetMapping("/tasks")
    public String tasksPage(
            @RequestParam(required = false) Integer projectId,
            @RequestParam(required = false) Integer sprintId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) Integer assigneeId,
            Model model) {

        List<Task> tasks = taskService.getAllTasksWithFilters(projectId, sprintId, status, priority, assigneeId);

        model.addAttribute("tasks", taskMapper.toDtoList(tasks));
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("sprints", sprintService.getAllSprints());
        model.addAttribute("users", taskService.getAllUsersForAssignment());

        model.addAttribute("selectedProjectId", projectId);
        model.addAttribute("selectedSprintId", sprintId);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedPriority", priority);
        model.addAttribute("selectedAssigneeId", assigneeId);

        return "tasks";
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/tasks/new")
    public String newTaskForm(
            @RequestParam(required = false) Integer selectedProjectId,
            Model model) {

        model.addAttribute("task", new TaskDto("", "", null, "TODO", null, null, null, null));

        model.addAttribute("projects", projectService.getAllProjects());

        if (selectedProjectId != null) {
            model.addAttribute("sprints", sprintService.findSprintsByProjectId(selectedProjectId));
        } else {
            model.addAttribute("sprints", List.of());
        }

        model.addAttribute("users", taskService.getAllUsersForAssignment());
        model.addAttribute("deadlineStr", "");
        model.addAttribute("selectedProjectId", selectedProjectId);
        model.addAttribute("error", null);

        return "task-form";
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/tasks/{id}/edit")
    public String editTask(@PathVariable Integer id, Model model) {
        Task task = taskService.getTaskById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        TaskDto dto = taskMapper.toDto(task);

        model.addAttribute("task", dto);
        model.addAttribute("projects", projectService.getAllProjects());

        if (task.getProject() != null) {
            model.addAttribute("sprints", sprintService.findSprintsByProjectId(task.getProject().getId()));
        } else {
            model.addAttribute("sprints", List.of());
        }

        model.addAttribute("users", taskService.getAllUsersForAssignment());
        model.addAttribute("deadlineStr", getDateStr(task.getDeadline()));
        model.addAttribute("selectedProjectId", task.getProject() != null ? task.getProject().getId() : null);
        model.addAttribute("error", null);

        return "task-form";
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @PostMapping("/tasks")
    public String createTask(@ModelAttribute TaskDto taskDto,
                             @RequestParam(value = "deadline", required = false) String deadlineStr,
                             @RequestParam(value = "assigneeId", required = false) Integer assigneeId,
                             Model model) {

        LocalDate deadline = parseLocalDate(deadlineStr);

        TaskDto dtoForValidation = new TaskDto(
                taskDto.name(), taskDto.description(), taskDto.priority(), taskDto.status(),
                deadline, taskDto.projectId(), taskDto.sprintId(), assigneeId
        );

        String error = validateTask(dtoForValidation);
        if (error != null) {
            model.addAttribute("task", taskDto);
            model.addAttribute("projects", projectService.getAllProjects());
            model.addAttribute("sprints", sprintService.getAllSprints());
            model.addAttribute("users", taskService.getAllUsersForAssignment());
            model.addAttribute("deadlineStr", deadlineStr != null ? deadlineStr : "");
            model.addAttribute("error", error);
            return "task-form";
        }

        Task task = taskMapper.toEntity(dtoForValidation);
        projectService.getProjectById(taskDto.projectId()).ifPresent(task::setProject);
        if (taskDto.sprintId() != null) {
            sprintService.getSprintById(taskDto.sprintId()).ifPresent(task::setSprint);
        }

        taskService.create(task, assigneeId);
        return "redirect:/tasks";
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @PostMapping("/tasks/{id}/update")
    public String updateTask(@PathVariable Integer id,
                             @ModelAttribute TaskDto taskDto,
                             @RequestParam(value = "deadline", required = false) String deadlineStr,
                             @RequestParam(value = "assigneeId", required = false) Integer assigneeId,
                             Model model) {

        LocalDate deadline = parseLocalDate(deadlineStr);

        TaskDto dtoForValidation = new TaskDto(
                taskDto.name(), taskDto.description(), taskDto.priority(), taskDto.status(),
                deadline, taskDto.projectId(), taskDto.sprintId(), assigneeId
        );

        String error = validateTask(dtoForValidation);
        if (error != null) {
            model.addAttribute("task", taskDto);
            model.addAttribute("projects", projectService.getAllProjects());
            model.addAttribute("sprints", sprintService.getAllSprints());
            model.addAttribute("users", taskService.getAllUsersForAssignment());
            model.addAttribute("deadlineStr", deadlineStr != null ? deadlineStr : "");
            model.addAttribute("error", error);
            return "task-form";
        }

        Task task = taskService.getTaskById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        taskMapper.updateEntity(task, dtoForValidation);

        projectService.getProjectById(taskDto.projectId()).ifPresent(task::setProject);

        if (taskDto.sprintId() != null && taskDto.sprintId() > 0) {
            sprintService.getSprintById(taskDto.sprintId())
                    .ifPresent(task::setSprint);
        } else if (taskDto.sprintId() != null && taskDto.sprintId() == 0) {
            task.setSprint(null);
        }

        taskService.update(task, assigneeId);
        return "redirect:/tasks";
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/tasks/{id}/delete")
    public String deleteTask(@PathVariable Integer id, Model model) {
        try {
            taskService.delete(id);
            return "redirect:/tasks";
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
        } catch (Exception e) {
            model.addAttribute("error", "Произошла ошибка при удалении задачи");
        }

        List<Task> tasks = taskService.getAllTasks();
        model.addAttribute("tasks", taskMapper.toDtoList(tasks));
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("sprints", sprintService.getAllSprints());
        model.addAttribute("users", taskService.getAllUsersForAssignment());
        return "tasks";
    }

    private String validateTask(TaskDto dto) {
        if (dto.name() == null || dto.name().trim().isEmpty()) {
            return "Название задачи обязательно";
        }
        if (dto.projectId() == null) {
            return "Выберите проект";
        }
        return null;
    }

    private String getDateStr(LocalDate date) {
        return date != null ? date.format(DateTimeFormatter.ISO_LOCAL_DATE) : "";
    }

    private LocalDate parseLocalDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) return null;
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception e) {
            return null;
        }
    }
}