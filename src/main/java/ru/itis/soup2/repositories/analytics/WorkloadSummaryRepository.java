package ru.itis.soup2.repositories.analytics;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.soup2.models.analytics.WorkloadSummary;

public interface WorkloadSummaryRepository extends JpaRepository<WorkloadSummary, Integer> {
}
