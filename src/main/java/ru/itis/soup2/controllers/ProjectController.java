package ru.itis.soup2.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itis.soup2.dto.ProjectDto;
import ru.itis.soup2.mappers.ProjectMapper;
import ru.itis.soup2.models.project.Project;
import ru.itis.soup2.repositories.core.UserRepository;
import ru.itis.soup2.services.project.ProjectService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectMapper projectMapper;
    private final UserRepository userRepository;

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/projects")
    public String projectsPage(Model model) {
        List<Project> projects = projectService.getAllProjects();
        model.addAttribute("projects", projectMapper.toDtoList(projects));
        return "projects";
    }

    // Форма создания
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/projects/new")
    public String newProjectForm(Model model) {
        model.addAttribute("project", new ProjectDto(null, "", "", null, null, null, null, null));
        model.addAttribute("managers", userRepository.findAll());
        model.addAttribute("startDateStr", "");
        model.addAttribute("endDateStr", "");
        model.addAttribute("error", null);
        return "project-form";
    }

    // Создание проекта
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @PostMapping("/projects")
    public String createProject(@ModelAttribute ProjectDto projectDto, Model model) {

        String error = validateProject(projectDto);

        if (error != null) {
            model.addAttribute("project", projectDto);
            model.addAttribute("managers", userRepository.findAll());
            model.addAttribute("startDateStr", getDateStr(projectDto.startDate()));
            model.addAttribute("endDateStr", getDateStr(projectDto.endDate()));
            model.addAttribute("error", error);
            return "project-form";
        }

        Project project = projectMapper.toEntity(projectDto);
        projectService.create(project);
        return "redirect:/projects";
    }

    // Форма редактирования
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/projects/{id}/edit")
    public String editProject(@PathVariable Integer id, Model model) {
        Project project = projectService.getProjectById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        ProjectDto dto = projectMapper.toDto(project);

        model.addAttribute("project", dto);
        model.addAttribute("managers", userRepository.findAll());
        model.addAttribute("startDateStr", getDateStr(project.getStartDate()));
        model.addAttribute("endDateStr", getDateStr(project.getEndDate()));
        model.addAttribute("error", null);
        return "project-form";
    }

    // Обновление проекта
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @PostMapping("/projects/{id}/update")
    public String updateProject(@PathVariable Integer id,
                                @ModelAttribute ProjectDto projectDto,
                                Model model) {

        String error = validateProject(projectDto);

        if (error != null) {
            model.addAttribute("project", projectDto);
            model.addAttribute("managers", userRepository.findAll());
            model.addAttribute("startDateStr", getDateStr(projectDto.startDate()));
            model.addAttribute("endDateStr", getDateStr(projectDto.endDate()));
            model.addAttribute("error", error);
            return "project-form";
        }

        Project project = projectService.getProjectById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        projectMapper.updateEntity(project, projectDto);
        projectService.update(project);
        return "redirect:/projects";
    }

    // ====================== ВАЛИДАЦИЯ ======================
    private String validateProject(ProjectDto dto) {
        if (dto.name() == null || dto.name().trim().isEmpty()) {
            return "Название проекта обязательно";
        }
        if (dto.managerId() == null) {
            return "Выберите менеджера";
        }
        if (dto.startDate() == null || dto.endDate() == null) {
            return "Обе даты обязательны";
        }
        if (dto.startDate().isAfter(dto.endDate())) {
            return "Дата начала должна быть раньше даты окончания";
        }
        return null;
    }

    private String getDateStr(LocalDate date) {
        return date != null ? date.format(DateTimeFormatter.ISO_LOCAL_DATE) : "";
    }
}