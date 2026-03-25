package ru.itis.soup2.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itis.soup2.models.project.Project;
import ru.itis.soup2.models.project.Sprint;
import ru.itis.soup2.models.project.Task;
import ru.itis.soup2.services.project.ProjectService;
import ru.itis.soup2.services.project.SprintService;
import ru.itis.soup2.services.project.TaskService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final SprintService sprintService;
    private final TaskService taskService;

    // ==================== TASKS (доступно всем авторизованным) ====================
    @GetMapping("/tasks")
    public String tasksDashboard(Model model) {
        List<Task> tasks = taskService.getAllTasks();
        model.addAttribute("tasks", tasks);
        return "tasks";                    // tasks.ftlh
    }

    // ==================== PROJECTS (только ROLE_MANAGER и ROLE_ADMIN) ====================
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/projects")
    public String projectsPage(Model model) {
        List<Project> projects = projectService.getAllProjects();
        model.addAttribute("projects", projects);
        return "projects";                 // projects.ftlh
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/projects/new")
    public String newProjectForm(Model model) {
        model.addAttribute("project", new Project());
        return "project-form";
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @PostMapping("/projects")
    public String createProject(@ModelAttribute Project project) {
        projectService.create(project);
        return "redirect:/projects";
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/projects/{id}/edit")
    public String editProject(@PathVariable Integer id, Model model) {
        Project project = projectService.getProjectById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        model.addAttribute("project", project);
        return "project-form";
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @PostMapping("/projects/{id}/update")
    public String updateProject(@PathVariable Integer id, @ModelAttribute Project project) {
        project.setId(id);   // важно для обновления
        projectService.update(project);
        return "redirect:/projects";
    }

    // ==================== SPRINTS (только ROLE_MANAGER и ROLE_ADMIN) ====================
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/sprints")
    public String sprintsPage(Model model) {
        List<Sprint> sprints = sprintService.getAllSprints();
        model.addAttribute("sprints", sprints);
        return "sprints";
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/sprints/new")
    public String newSprintForm(Model model) {
        model.addAttribute("sprint", new Sprint());
        return "sprint-form";
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @PostMapping("/sprints")
    public String createSprint(@ModelAttribute Sprint sprint) {
        sprintService.create(sprint);
        return "redirect:/sprints";
    }
}