package ru.itis.soup2.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.soup2.dto.ProjectDto;
import ru.itis.soup2.models.enums.ProjectStatus;
import ru.itis.soup2.models.project.Project;
import ru.itis.soup2.models.core.User;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProjectMapper {
    public ProjectDto toDto(Project project) {
        if (project == null) return null;

        User manager = project.getManager();
        return new ProjectDto(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getStartDate(),
                project.getEndDate(),
                project.getStatus() != null ? project.getStatus().name() : null,
                manager != null ? manager.getId() : null,
                manager != null ? manager.getName() : null
        );
    }

    public List<ProjectDto> toDtoList(List<Project> projects) {
        return projects.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Project toEntity(ProjectDto dto) {
        if (dto == null) return null;

        Project project = new Project();
        project.setName(dto.name());
        project.setDescription(dto.description());
        project.setStartDate(dto.startDate());
        project.setEndDate(dto.endDate());
        if (dto.status() != null) {
            project.setStatus(ProjectStatus.valueOf(dto.status()));
        }
        // менеджер устанавливается отдельно в сервисе через UserRepository
        return project;
    }

    public void updateEntity(Project project, ProjectDto dto) {
        if (dto == null || project == null) return;

        project.setName(dto.name());
        project.setDescription(dto.description());
        project.setStartDate(dto.startDate());
        project.setEndDate(dto.endDate());
        if (dto.status() != null) {
            project.setStatus(ProjectStatus.valueOf(dto.status()));
        }
    }
}