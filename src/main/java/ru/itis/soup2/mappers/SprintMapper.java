package ru.itis.soup2.mappers;

import ru.itis.soup2.dto.SprintDto;
import ru.itis.soup2.models.project.Sprint;
import java.util.List;
import java.util.stream.Collectors;

public final class SprintMapper {

    private SprintMapper() {}

    public static SprintDto toDto(Sprint sprint) {
        if (sprint == null) return null;
        return new SprintDto(
                sprint.getId(),
                sprint.getName(),
                sprint.getStartDate(),
                sprint.getEndDate(),
                sprint.getProject() != null ? sprint.getProject().getId() : null,
                sprint.getProject() != null ? sprint.getProject().getName() : null
        );
    }

    public static List<SprintDto> toDtoList(List<Sprint> sprints) {
        return sprints.stream()
                .map(SprintMapper::toDto)
                .collect(Collectors.toList());
    }
}