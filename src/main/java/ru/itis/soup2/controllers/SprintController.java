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

    // ====================== СПИСОК СПРИНТОВ ======================
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/sprints")
    public String sprintsPage(Model model) {
        List<Sprint> sprints = sprintService.getAllSprints();
        model.addAttribute("sprints", sprintMapper.toDtoList(sprints));
        return "sprints";
    }

    // ====================== ФОРМА СОЗДАНИЯ ======================
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/sprints/new")
    public String newSprintForm(Model model) {
        model.addAttribute("sprint", new SprintDto(null, "", null, null, null, null));
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("startDateStr", "");
        model.addAttribute("endDateStr", "");
        model.addAttribute("error", null);
        return "sprint-form";
    }

    // ====================== СОЗДАНИЕ СПРИНТА ======================
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @PostMapping("/sprints")
    public String createSprint(@ModelAttribute SprintDto sprintDto,
                               @RequestParam("startDate") String startDateStr,
                               @RequestParam("endDate") String endDateStr,
                               Model model) {

        // Парсим даты перед валидацией
        LocalDate startDate = parseLocalDate(startDateStr);
        LocalDate endDate = parseLocalDate(endDateStr);

        // Создаём DTO с распарсенными датами для валидации
        SprintDto dtoForValidation = new SprintDto(
                sprintDto.id(),
                sprintDto.name(),
                startDate,
                endDate,
                sprintDto.projectId(),
                sprintDto.projectName()
        );

        String error = validateSprint(dtoForValidation);

        if (error != null) {
            model.addAttribute("sprint", sprintDto);           // возвращаем то, что пришло от пользователя
            model.addAttribute("projects", projectService.getAllProjects());
            model.addAttribute("startDateStr", startDateStr);
            model.addAttribute("endDateStr", endDateStr);
            model.addAttribute("error", error);
            return "sprint-form";
        }

        SprintDto correctedDto = new SprintDto(
                null,
                sprintDto.name(),
                startDate,
                endDate,
                sprintDto.projectId(),
                null
        );

        Sprint sprint = sprintMapper.toEntity(correctedDto);
        projectService.getProjectById(correctedDto.projectId())
                .ifPresent(sprint::setProject);

        sprintService.create(sprint);
        return "redirect:/sprints";
    }

    // ====================== ФОРМА РЕДАКТИРОВАНИЯ ======================
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

    // ====================== ОБНОВЛЕНИЕ СПРИНТА ======================
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
                sprintDto.id(),
                sprintDto.name(),
                startDate,
                endDate,
                sprintDto.projectId(),
                sprintDto.projectName()
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
                sprintDto.id(),
                sprintDto.name(),
                startDate,
                endDate,
                sprintDto.projectId(),
                null
        );

        sprintMapper.updateEntity(sprint, correctedDto);

        if (correctedDto.projectId() != null) {
            projectService.getProjectById(correctedDto.projectId())
                    .ifPresent(sprint::setProject);
        }

        sprintService.update(sprint);
        return "redirect:/sprints";
    }

    // ====================== ВАЛИДАЦИЯ ======================
    private String validateSprint(SprintDto dto) {
        if (dto.name() == null || dto.name().trim().isEmpty()) {
            return "Название спринта обязательно";
        }
        if (dto.projectId() == null) {
            return "Выберите проект";
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