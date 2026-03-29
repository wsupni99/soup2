package ru.itis.soup2.mappers;

import org.springframework.stereotype.Component;
import ru.itis.soup2.dto.WorkloadSummaryDto;
import ru.itis.soup2.models.analytics.WorkloadSummary;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WorkloadSummaryMapper {
    public WorkloadSummaryDto toDto(WorkloadSummary summary) {
        if (summary == null) return null;

        return new WorkloadSummaryDto(
                summary.getId(),
                summary.getUser() != null ? summary.getUser().getId() : null,
                summary.getUser() != null ? summary.getUser().getName() : null,
                summary.getProject() != null ? summary.getProject().getId() : null,
                summary.getProject() != null ? summary.getProject().getName() : null,
                summary.getSprint() != null ? summary.getSprint().getId() : null,
                summary.getSprint() != null ? summary.getSprint().getName() : null,
                summary.getOpenTasksCount(),
                summary.getClosedTasksCount()
        );
    }

    public List<WorkloadSummaryDto> toDtoList(List<WorkloadSummary> summaries) {
        return summaries.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}