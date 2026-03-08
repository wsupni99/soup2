package ru.itis.soup2.services.analytics;

import ru.itis.soup2.models.analytics.WorkloadSummary;

import java.util.List;

public interface WorkloadSummaryService {
    List<WorkloadSummary> findByUserId(Integer userId);
    List<WorkloadSummary> findByProjectId(Integer projectId);
    List<WorkloadSummary> findAll();
}