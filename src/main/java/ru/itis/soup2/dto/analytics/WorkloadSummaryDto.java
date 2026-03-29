package ru.itis.soup2.dto.analytics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkloadSummaryDto {

    private Integer id;
    private Integer userId;
    private String userName;
    private Integer projectId;
    private String projectName;
    private Integer sprintId;
    private String sprintName;
    private Integer openTasksCount;
    private Integer closedTasksCount;
}