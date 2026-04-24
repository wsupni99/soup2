package ru.itis.soup2.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itis.soup2.dto.project.SprintDto;
import ru.itis.soup2.mappers.project.SprintMapper;
import ru.itis.soup2.models.project.Sprint;
import ru.itis.soup2.services.project.ProjectService;
import ru.itis.soup2.services.project.SprintService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SprintController {

    private final SprintService sprintService;
    private final ProjectService projectService;
    private final SprintMapper sprintMapper;

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/sprints")
    public String sprintsPage(Model model) {
        List<Sprint> sprints = sprintService.getAllSprints();
        model.addAttribute("sprints", sprintMapper.toDtoList(sprints));
        return "sprints/sprints";
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/sprints/new")
    public String newSprintForm(Model model) {
        model.addAttribute("sprint", new SprintDto());
        model.addAttribute("projects", projectService.getAllProjects());
        return "sprints/sprint-form";
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @PostMapping("/sprints")
    public String createSprint(@ModelAttribute SprintDto sprintDto, Model model) {
        String error = validateSprint(sprintDto);

        if (error != null) {
            model.addAttribute("sprint", sprintDto);
            model.addAttribute("projects", projectService.getAllProjects());
            model.addAttribute("error", error);
            return "sprints/sprint-form";
        }

        Sprint sprint = sprintMapper.toEntity(sprintDto);
        projectService.getProjectById(sprintDto.getProjectId())
                .ifPresent(sprint::setProject);

        sprintService.create(sprint);
        return "redirect:/sprints";
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/sprints/{id}/edit")
    public String editSprint(@PathVariable Integer id, Model model) {
        Sprint sprint = sprintService.getSprintById(id)
                .orElseThrow(() -> new RuntimeException("Sprint not found"));

        SprintDto dto = sprintMapper.toDto(sprint);
        model.addAttribute("sprint", dto);
        model.addAttribute("projects", projectService.getAllProjects());
        return "sprints/sprint-form";
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @PostMapping("/sprints/{id}/update")
    public String updateSprint(@PathVariable Integer id,
                               @ModelAttribute SprintDto sprintDto,
                               Model model) {

        String error = validateSprint(sprintDto);

        if (error != null) {
            model.addAttribute("sprint", sprintDto);
            model.addAttribute("projects", projectService.getAllProjects());
            model.addAttribute("error", error);
            return "sprints/sprint-form";
        }

        Sprint sprint = sprintService.getSprintById(id)
                .orElseThrow(() -> new RuntimeException("Sprint not found"));

        sprintMapper.updateEntity(sprint, sprintDto);

        if (sprintDto.getProjectId() != null) {
            projectService.getProjectById(sprintDto.getProjectId())
                    .ifPresent(sprint::setProject);
        }

        sprintService.update(sprint);
        return "redirect:/sprints";
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/sprints/{id}/delete")
    public String deleteSprint(@PathVariable Integer id, Model model) {
        try {
            sprintService.delete(id);
            return "redirect:/sprints";
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
        } catch (Exception e) {
            model.addAttribute("error", "Произошла ошибка при удалении спринта");
        }

        List<Sprint> sprints = sprintService.getAllSprints();
        model.addAttribute("sprints", sprintMapper.toDtoList(sprints));
        return "sprints/sprints";
    }

    private String validateSprint(SprintDto dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            return "Название спринта обязательно";
        }
        if (dto.getProjectId() == null) {
            return "Выберите проект";
        }
        if (dto.getStartDate() == null || dto.getEndDate() == null) {
            return "Обе даты обязательны";
        }
        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            return "Дата начала должна быть раньше даты окончания";
        }
        return null;
    }
}