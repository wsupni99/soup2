package ru.itis.soup2.services.analytics;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.soup2.models.analytics.WorkloadSummary;
import ru.itis.soup2.repositories.analytics.WorkloadSummaryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkloadSummaryServiceImpl implements WorkloadSummaryService {
    private final WorkloadSummaryRepository workloadSummaryRepository;

    @Override
    public List<WorkloadSummary> findByUserId(Integer userId) {
        return workloadSummaryRepository.findByUserId(userId);
    }

    @Override
    public List<WorkloadSummary> findByProjectId(Integer projectId) {
        return workloadSummaryRepository.findByProjectId(projectId);
    }

    @Override
    public List<WorkloadSummary> findAll() {
        return workloadSummaryRepository.findAll();
    }
}
