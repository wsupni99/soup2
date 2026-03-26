package ru.itis.soup2.mappers;

import org.springframework.stereotype.Component;
import ru.itis.soup2.dto.SprintDto;
import ru.itis.soup2.dto.TaskDto;
import ru.itis.soup2.models.project.Sprint;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SprintMapper {

    private final TaskMapper taskMapper;

    public SprintMapper(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    public SprintDto toDto(Sprint sprint) {
        if (sprint == null) return null;

        List<TaskDto> taskDtos = sprint.getTasks() != null ?
                taskMapper.toDtoList(sprint.getTasks()) :
                List.of();

        return new SprintDto(
                sprint.getId(),
                sprint.getName(),
                sprint.getStartDate(),
                sprint.getEndDate(),
                sprint.getProject() != null ? sprint.getProject().getId() : null,
                sprint.getProject() != null ? sprint.getProject().getName() : null,
                taskDtos
        );
    }

    public List<SprintDto> toDtoList(List<Sprint> sprints) {
        return sprints.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Sprint toEntity(SprintDto dto) {
        if (dto == null) return null;

        return Sprint.builder()
                .id(dto.id())
                .name(dto.name())
                .startDate(dto.startDate())
                .endDate(dto.endDate())
                .build();
    }

    public void updateEntity(Sprint sprint, SprintDto dto) {
        if (dto == null || sprint == null) return;

        sprint.setName(dto.name());
        sprint.setStartDate(dto.startDate());
        sprint.setEndDate(dto.endDate());
    }
}