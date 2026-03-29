package ru.itis.soup2.mappers.project;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.soup2.dto.project.ProjectDto;
import ru.itis.soup2.models.enums.ProjectStatus;
import ru.itis.soup2.models.project.Project;
import ru.itis.soup2.models.core.User;
import ru.itis.soup2.repositories.core.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProjectMapper {

    private final UserRepository userRepository;

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
        return projects.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Project toEntity(ProjectDto dto) {
        if (dto == null) return null;

        Project project = new Project();
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());
        if (dto.getStatus() != null) {
            project.setStatus(ProjectStatus.valueOf(dto.getStatus()));
        }

        setManager(project, dto.getManagerId());
        return project;
    }

    public void updateEntity(Project project, ProjectDto dto) {
        if (dto == null || project == null) return;

        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());
        if (dto.getStatus() != null) {
            project.setStatus(ProjectStatus.valueOf(dto.getStatus()));
        }

        setManager(project, dto.getManagerId());
    }

    private void setManager(Project project, Integer managerId) {
        if (managerId != null) {
            User manager = userRepository.findById(managerId)
                    .orElseThrow(() -> new EntityNotFoundException("Manager not found with id: " + managerId));
            project.setManager(manager);
        }
    }
}