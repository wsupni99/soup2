package ru.itis.soup2.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.itis.soup2.dto.core.UserWithRoleDto;
import ru.itis.soup2.dto.project.AttachmentDto;
import ru.itis.soup2.dto.project.CommentDto;
import ru.itis.soup2.dto.project.SprintDto;
import ru.itis.soup2.dto.project.TaskDto;
import ru.itis.soup2.mappers.project.SprintMapper;
import ru.itis.soup2.mappers.project.TaskMapper;
import ru.itis.soup2.models.core.User;
import ru.itis.soup2.models.enums.TaskPriority;
import ru.itis.soup2.models.enums.TaskStatus;
import ru.itis.soup2.models.project.Task;
import ru.itis.soup2.security.CustomUserDetails;
import ru.itis.soup2.security.OAuth2TokenService;
import ru.itis.soup2.services.integration.GoogleTasksService;
import ru.itis.soup2.services.project.CommentService;
import ru.itis.soup2.services.project.ProjectService;
import ru.itis.soup2.services.project.SprintService;
import ru.itis.soup2.services.project.TaskService;

import java.time.LocalDate;
import java.util.List;
@Slf4j
@Controller
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final ProjectService projectService;
    private final SprintService sprintService;
    private final TaskMapper taskMapper;
    private final SprintMapper sprintMapper;
    private final CommentService commentService;
    private final GoogleTasksService googleTasksService;
    private final OAuth2TokenService oAuth2TokenService;


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/tasks")
    public String tasksPage(Model model,
                            @AuthenticationPrincipal CustomUserDetails userDetails,
                            @RequestParam(required = false) Integer projectId,
                            @RequestParam(required = false) Integer sprintId,
                            @RequestParam(required = false) TaskStatus status,
                            @RequestParam(required = false) TaskPriority priority,
                            @RequestParam(required = false) Integer assigneeId,
                            @RequestParam(required = false) String search) {

        List<Task> tasksList = taskService.getAllTasksWithFilters(
                projectId, sprintId, status, priority, assigneeId, search);

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

        model.addAttribute("currentUserRole", getCurrentUserRole(userDetails));

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

        TaskDto dto = taskMapper.toDto(task);

        model.addAttribute("task", dto);
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("sprints", getSprintsByProject(task));
        model.addAttribute("projectUsers", getProjectUsers(task));

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
    public String updateTask(@PathVariable("id") Integer id,
                             @ModelAttribute TaskDto taskDto,
                             RedirectAttributes redirectAttributes) {
        Task task = taskService.getTaskById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        taskMapper.updateEntity(task, taskDto);
        taskService.update(task, taskDto.getAssigneeId());
        redirectAttributes.addFlashAttribute("successMessage", "Задача успешно обновлена");
        return "redirect:/tasks/" + id;
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @PostMapping("/tasks/{id}/delete")
    public String deleteTask(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            Task task = taskService.getTaskById(id)
                    .orElseThrow(() -> new RuntimeException("Task not found"));

            String taskName = task.getName();
            taskService.delete(id);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Задача №" + id + " \"" + taskName + "\" и все связанные данные успешно удалены");

            return "redirect:/tasks";
        } catch (Exception e) {
            log.error("Ошибка при удалении задачи {}", id, e);
            redirectAttributes.addFlashAttribute("errorMessage", "Не удалось удалить задачу: " + e.getMessage());
            return "redirect:/tasks";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/sprints/byProject")
    @ResponseBody
    public List<SprintDto> getSprintsByProject(@RequestParam Integer projectId) {
        return sprintService.findSprintsByProjectId(projectId)
                .stream()
                .map(sprintMapper::toDto)
                .toList();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/tasks/{id}")
    public String taskDetail(@PathVariable Integer id,
                             Model model,
                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        Task task = taskService.getTaskById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        List<CommentDto> comments = commentService.findByTaskId(id)
                .stream()
                .map(CommentDto::from)
                .toList();

        model.addAttribute("task", taskMapper.toDto(task));
        model.addAttribute("currentUserRole", getCurrentUserRole(userDetails));
        model.addAttribute("users", taskService.getAllUsersForAssignment());
        model.addAttribute("comments", comments);
        model.addAttribute("commentsCount", comments.size());
        model.addAttribute("attachmentsCount", task.getAttachments().size());

        return "tasks/task-detail";
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @PostMapping("/tasks/{parentId}/subtasks")
    @ResponseBody
    public TaskDto createSubTask(@PathVariable("parentId") Integer parentId,
                                 @RequestParam String name,
                                 @RequestParam(required = false) Integer assigneeId,
                                 @RequestParam(required = false) LocalDate deadline,
                                 @RequestParam(required = false, defaultValue = "MEDIUM") TaskPriority priority) {

        Task subTask = Task.builder()
                .name(name.trim())
                .priority(priority)
                .build();

        Task saved = taskService.createSubTask(parentId, subTask, assigneeId, deadline);
        return taskMapper.toDto(saved);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/tasks/{taskId}/attachments")
    @ResponseBody
    public AttachmentDto uploadAttachment(@PathVariable Integer taskId,
                                          @RequestParam("file") MultipartFile file) {
        Task task = taskService.addAttachment(taskId, file);
        return task.getAttachments().isEmpty()
                ? null
                : AttachmentDto.from(task.getAttachments().get(task.getAttachments().size() - 1));
    }

    @GetMapping("/users/byProject")
    @ResponseBody
    public List<UserWithRoleDto> getUsersByProject(@RequestParam Integer projectId) {
        return taskService.getUsersByProjectId(projectId).stream()
                .map(user -> UserWithRoleDto.builder()
                        .userId(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .roleName(user.getRole() != null ? user.getRole().getRoleName() : null)
                        .build())
                .toList();
    }

    // ====================== Google Tasks ======================

    @PostMapping("/tasks/{id}/google-tasks")
    @ResponseBody
    public boolean addToGoogleTasks(@PathVariable Integer id,
                                    @AuthenticationPrincipal CustomUserDetails userDetails) {

        Task task = taskService.getTaskById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!canAddToGoogleTasks(task, userDetails)) {
            throw new AccessDeniedException("Нет прав для добавления задачи в Google Tasks");
        }

        String accessToken = oAuth2TokenService.getGoogleAccessToken(userDetails.getUsername());

        if (accessToken == null) {
            log.warn("Пользователь {} попытался добавить задачу без Google токена", userDetails.getUsername());
            return false;
        }

        boolean success = googleTasksService.addTaskToGoogleTasks(task, accessToken);
        return success;
    }

    // ====================== Вспомогательные методы ======================

    private String getCurrentUserRole(CustomUserDetails userDetails) {
        if (userDetails == null || userDetails.getUser().getRole() == null) {
            return "ROLE_USER";
        }
        return userDetails.getUser().getRole().getRoleName();
    }

    private boolean canAddToGoogleTasks(Task task, CustomUserDetails userDetails) {
        if (userDetails == null) {
            return false;
        }

        String role = getCurrentUserRole(userDetails);

        // Менеджер и Админ могут добавлять любую задачу
        if ("ROLE_MANAGER".equals(role) || "ROLE_ADMIN".equals(role)) {
            return true;
        }

        // Разработчик может добавлять только назначенные на него задачи
        if ("ROLE_DEVELOPER".equals(role)) {
            return task.getAssignee() != null &&
                    task.getAssignee().getId().equals(userDetails.getUser().getId());
        }

        return false;
    }

    private List<SprintDto> getSprintsByProject(Task task) {
        if (task.getProject() == null) return List.of();
        return sprintService.findSprintsByProjectId(task.getProject().getId())
                .stream()
                .map(sprintMapper::toDto)
                .toList();
    }

    private List<User> getProjectUsers(Task task) {
        if (task.getProject() == null) {
            return taskService.getAllUsersForAssignment();
        }
        return taskService.getUsersByProjectId(task.getProject().getId());
    }
}