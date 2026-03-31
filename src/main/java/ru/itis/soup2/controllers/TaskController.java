package ru.itis.soup2.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itis.soup2.dto.project.SprintDto;
import ru.itis.soup2.dto.project.TaskDto;
import ru.itis.soup2.mappers.project.SprintMapper;
import ru.itis.soup2.mappers.project.TaskMapper;
import ru.itis.soup2.models.project.Task;
import ru.itis.soup2.security.CustomUserDetails;
import ru.itis.soup2.services.project.ProjectService;
import ru.itis.soup2.services.project.SprintService;
import ru.itis.soup2.services.project.TaskService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final ProjectService projectService;
    private final SprintService sprintService;
    private final TaskMapper taskMapper;
    private final SprintMapper sprintMapper;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/tasks")
    public String tasksPage(Model model,
                            @AuthenticationPrincipal CustomUserDetails userDetails,
                            @RequestParam(required = false) Integer projectId,
                            @RequestParam(required = false) Integer sprintId,
                            @RequestParam(required = false) String status,
                            @RequestParam(required = false) String priority,
                            @RequestParam(required = false) Integer assigneeId,
                            @RequestParam(required = false) String search) {

        List<Task> tasksList = taskService.getAllTasksWithFilters(projectId, sprintId, status, priority, assigneeId, search);

        model.addAttribute("tasks", taskMapper.toDtoList(tasksList));
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("sprints", sprintService.getAllSprints());
        model.addAttribute("users", taskService.getAllUsersForAssignment());

        model.addAttribute("selectedProjectId", projectId);
        model.addAttribute("selectedSprintId", sprintId);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedPriority", priority);
        model.addAttribute("selectedAssigneeId", assigneeId);
        model.addAttribute("selectedSearch", search);

        String currentUserRole = (userDetails != null && userDetails.getUser().getRole() != null)
                ? userDetails.getUser().getRole().getRoleName()
                : "ROLE_USER";
        model.addAttribute("currentUserRole", currentUserRole);

        return "tasks/tasks";
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/tasks/new")
    public String newTaskForm(Model model) {
        model.addAttribute("task", new TaskDto());
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("sprints", List.of());
        model.addAttribute("users", taskService.getAllUsersForAssignment());
        return "tasks/task-form";
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/tasks/{id}/edit")
    public String editTask(@PathVariable("id") Integer id, Model model) {
        Task task = taskService.getTaskById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        model.addAttribute("task", taskMapper.toDto(task));
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("sprints", task.getProject() != null
                ? sprintService.findSprintsByProjectId(task.getProject().getId())
                : List.of());
        model.addAttribute("users", taskService.getAllUsersForAssignment());
        return "tasks/task-form";
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @PostMapping("/tasks")
    public String createTask(@ModelAttribute TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        taskService.create(task, taskDto.getAssigneeId());
        return "redirect:/tasks";
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @PostMapping("/tasks/{id}/update")
    public String updateTask(@PathVariable("id") Integer id, @ModelAttribute TaskDto taskDto) {
        Task task = taskService.getTaskById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        taskMapper.updateEntity(task, taskDto);
        taskService.update(task, taskDto.getAssigneeId());
        return "redirect:/tasks";
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/tasks/{id}/delete")
    public String deleteTask(@PathVariable("id") Integer id) {
        taskService.delete(id);
        return "redirect:/tasks";
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/sprints/byProject")
    @ResponseBody
    // AJAX
    public List<SprintDto> getSprintsByProject(@RequestParam Integer projectId) {
        return sprintService.findSprintsByProjectId(projectId)
                .stream()
                .map(sprintMapper::toDto)
                .toList();
    }
}