package ru.itis.soup2.models.analytics;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.soup2.models.core.User;
import ru.itis.soup2.models.project.Project;
import ru.itis.soup2.models.project.Sprint;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "workload_summary", schema = "analytics")
public class WorkloadSummary {
    @Id
    @Column(name = "summary_id")
    private Integer summaryId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "sprint_id")
    private Sprint sprint;

    @Column(name = "open_tasks_count")
    private Integer openTasksCount;

    @Column(name = "closed_tasks_count")
    private Integer closedTasksCount;
}
