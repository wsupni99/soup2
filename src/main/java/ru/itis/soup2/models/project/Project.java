package ru.itis.soup2.models.project;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.soup2.models.core.User;
import ru.itis.soup2.models.enums.ProjectStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "projects", schema = "project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Integer id;

    private String name;
    private String description;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @OneToMany(mappedBy = "project")
    private List<Sprint> sprints = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    private List<Task> tasks = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;
}
