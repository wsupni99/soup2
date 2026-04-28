package ru.itis.soup2.services.project;

import ru.itis.soup2.models.project.ProjectRequest;

import java.util.List;

public interface ProjectRequestService {
    ProjectRequest createRequest(Integer projectId, Integer userId);
    ProjectRequest approveRequest(Long requestId, Integer managerId);
    ProjectRequest rejectRequest(Long requestId, Integer managerId);

    List<ProjectRequest> getMyPendingRequests(Integer userId);
    List<ProjectRequest> getPendingRequestsForManager(Integer managerId);

    boolean hasActiveRequest(Integer userId, Integer projectId);
}