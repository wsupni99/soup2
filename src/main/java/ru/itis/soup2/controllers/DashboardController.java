package ru.itis.soup2.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.itis.soup2.dto.project.TaskDto;
import ru.itis.soup2.mappers.project.TaskMapper;
import ru.itis.soup2.models.enums.TaskStatus;
import ru.itis.soup2.models.project.Project;
import ru.itis.soup2.models.project.Task;
import ru.itis.soup2.security.CustomUserDetails;
import ru.itis.soup2.services.project.ProjectService;
import ru.itis.soup2.services.project.TaskService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated() and !hasRole('ROLE_ADMIN')")
public class DashboardController {

    private final TaskService taskService;
    private final ProjectService projectService;
    private final TaskMapper taskMapper;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails,
                            Model model) {

        Integer userId = userDetails.getUser().getId();
        List<Project> userProjects = projectService.getProjectsByUserId(userId);

        model.addAttribute("userProjects", userProjects);
        model.addAttribute("currentUserId", userId);

        // Если есть проекты — показываем первый по умолчанию
        if (!userProjects.isEmpty()) {
            return "redirect:/dashboard/" + userProjects.get(0).getId();
        }

        return "dashboard/dashboard";
    }

    @GetMapping("/dashboard/{projectId}")
    public String projectDashboard(@PathVariable Integer projectId,
                                   @AuthenticationPrincipal CustomUserDetails userDetails,
                                   Model model) {

        Integer userId = userDetails.getUser().getId();

        // Проверяем, что пользователь состоит в проекте
        if (!projectService.isUserInProject(userId, projectId)) {
            return "redirect:/dashboard";
        }

        Project project = projectService.getProjectById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        List<Task> tasks = taskService.getTasksByProjectId(projectId);

        model.addAttribute("project", project);
        model.addAttribute("tasks", taskMapper.toDtoList(tasks));
        model.addAttribute("userProjects", projectService.getProjectsByUserId(userId));

        return "dashboard/dashboard";
    }

    @PostMapping("/dashboard/tasks/{taskId}/status")
    @ResponseBody
    public TaskDto updateTaskStatus(@PathVariable Integer taskId,
                                    @RequestParam TaskStatus status) {

        Task task = taskService.getTaskById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus(status);
        taskService.updateStatusOnly(task); // новый метод, только меняет статус

        return taskMapper.toDto(task);
    }
}