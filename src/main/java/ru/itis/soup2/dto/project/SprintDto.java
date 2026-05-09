package ru.itis.soup2.dto.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SprintDto {

    private Integer id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer projectId;
    private String projectName;
    private List<TaskDto> tasks;
    private Integer taskCount;

    public SprintDto(Integer id, String name, LocalDate startDate, LocalDate endDate,
                     Integer projectId, String projectName, List<TaskDto> tasks) {
        this(id, name, startDate, endDate, projectId, projectName,
                tasks != null ? tasks : List.of(),
                tasks != null ? tasks.size() : 0);
    }
}