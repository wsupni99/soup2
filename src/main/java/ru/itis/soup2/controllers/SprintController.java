package ru.itis.soup2.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itis.soup2.dto.SprintDto;
import ru.itis.soup2.mappers.SprintMapper;
import ru.itis.soup2.models.project.Sprint;
import ru.itis.soup2.services.project.ProjectService;
import ru.itis.soup2.services.project.SprintService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        return "sprints";
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/sprints/new")
    public String newSprintForm(Model model) {
        model.addAttribute("sprint", new SprintDto());
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("startDateStr", "");
        model.addAttribute("endDateStr", "");
        model.addAttribute("error", null);
        return "sprint-form";
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @PostMapping("/sprints")
    public String createSprint(@ModelAttribute SprintDto sprintDto,
                               @RequestParam("startDate") String startDateStr,
                               @RequestParam("endDate") String endDateStr,
                               Model model) {

        LocalDate startDate = parseLocalDate(startDateStr);
        LocalDate endDate = parseLocalDate(endDateStr);

        SprintDto dtoForValidation = new SprintDto(
                null,
                sprintDto.getName(),
                startDate,
                endDate,
                sprintDto.getProjectId(),
                null
        );

        String error = validateSprint(dtoForValidation);

        if (error != null) {
            model.addAttribute("sprint", sprintDto);
            model.addAttribute("projects", projectService.getAllProjects());
            model.addAttribute("startDateStr", startDateStr);
            model.addAttribute("endDateStr", endDateStr);
            model.addAttribute("error", error);
            return "sprint-form";
        }

        Sprint sprint = sprintMapper.toEntity(dtoForValidation);
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
        model.addAttribute("startDateStr", getDateStr(sprint.getStartDate()));
        model.addAttribute("endDateStr", getDateStr(sprint.getEndDate()));
        model.addAttribute("error", null);
        return "sprint-form";
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @PostMapping("/sprints/{id}/update")
    public String updateSprint(@PathVariable Integer id,
                               @ModelAttribute SprintDto sprintDto,
                               @RequestParam("startDate") String startDateStr,
                               @RequestParam("endDate") String endDateStr,
                               Model model) {

        LocalDate startDate = parseLocalDate(startDateStr);
        LocalDate endDate = parseLocalDate(endDateStr);

        SprintDto dtoForValidation = new SprintDto(
                null,
                sprintDto.getName(),
                startDate,
                endDate,
                sprintDto.getProjectId(),
                null
        );

        String error = validateSprint(dtoForValidation);

        if (error != null) {
            model.addAttribute("sprint", sprintDto);
            model.addAttribute("projects", projectService.getAllProjects());
            model.addAttribute("startDateStr", startDateStr);
            model.addAttribute("endDateStr", endDateStr);
            model.addAttribute("error", error);
            return "sprint-form";
        }

        Sprint sprint = sprintService.getSprintById(id)
                .orElseThrow(() -> new RuntimeException("Sprint not found"));

        SprintDto correctedDto = new SprintDto(
                null,
                sprintDto.getName(),
                startDate,
                endDate,
                sprintDto.getProjectId(),
                null
        );

        sprintMapper.updateEntity(sprint, correctedDto);

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
            List<Sprint> sprints = sprintService.getAllSprints();
            model.addAttribute("sprints", sprintMapper.toDtoList(sprints));
            return "sprints";
        } catch (Exception e) {
            model.addAttribute("error", "Произошла ошибка при удалении спринта");
            List<Sprint> sprints = sprintService.getAllSprints();
            model.addAttribute("sprints", sprintMapper.toDtoList(sprints));
            return "sprints";
        }
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

    private String getDateStr(LocalDate date) {
        return date != null ? date.format(DateTimeFormatter.ISO_LOCAL_DATE) : "";
    }

    private LocalDate parseLocalDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception e) {
            return null;
        }
    }
}