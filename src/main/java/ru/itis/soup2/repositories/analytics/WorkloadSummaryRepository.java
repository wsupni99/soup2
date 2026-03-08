package ru.itis.soup2.repositories.analytics;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.soup2.models.analytics.WorkloadSummary;

import java.util.List;

public interface WorkloadSummaryRepository extends JpaRepository<WorkloadSummary, Long> {
    List<WorkloadSummary> findByUserId(Integer userId);
    List<WorkloadSummary> findByProjectId(Integer projectId);
}
