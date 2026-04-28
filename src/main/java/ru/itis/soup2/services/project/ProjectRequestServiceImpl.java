package ru.itis.soup2.services.project;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.soup2.models.core.User;
import ru.itis.soup2.models.enums.RequestStatus;
import ru.itis.soup2.models.project.Project;
import ru.itis.soup2.models.project.ProjectRequest;
import ru.itis.soup2.repositories.core.UserRepository;
import ru.itis.soup2.repositories.project.ProjectRepository;
import ru.itis.soup2.repositories.project.ProjectRequestRepository;
import ru.itis.soup2.services.core.UserService;
import ru.itis.soup2.services.project.ProjectRequestService;
import ru.itis.soup2.services.project.ProjectService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectRequestServiceImpl implements ProjectRequestService {

    private final ProjectRequestRepository requestRepository;
    private final ProjectRepository projectRepository;
    private final ProjectService projectService;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public ProjectRequest createRequest(Integer projectId, Integer userId) {
        if (projectService.isUserInProject(userId, projectId)) {
            throw new IllegalStateException("Вы уже являетесь участником этого проекта");
        }

        if (hasActiveRequest(userId, projectId)) {
            throw new IllegalStateException("Заявка на вступление уже подана");
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Проект не найден"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        ProjectRequest request = ProjectRequest.builder()
                .project(project)
                .user(user)
                .status(RequestStatus.PENDING)
                .build();

        return requestRepository.save(request);
    }

    @Transactional
    @Override
    public ProjectRequest approveRequest(Long requestId, Integer managerId) {
        ProjectRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Заявка не найдена"));

        if (!request.getProject().getManager().getId().equals(managerId)) {
            throw new IllegalStateException("У вас нет прав на одобрение этой заявки");
        }
        request.setStatus(RequestStatus.APPROVED);
        requestRepository.save(request);

        projectService.addUserToProject(
                request.getProject().getId(),
                request.getUser().getId()
        );
        return request;
    }

    @Transactional
    @Override
    public ProjectRequest rejectRequest(Long requestId, Integer managerId) {
        ProjectRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Заявка не найдена"));

        if (!request.getProject().getManager().getId().equals(managerId)) {
            throw new IllegalStateException("У вас нет прав на отклонение этой заявки");
        }

        request.setStatus(RequestStatus.REJECTED);
        return requestRepository.save(request);
    }

    @Override
    public List<ProjectRequest> getMyPendingRequests(Integer userId) {
        return requestRepository.findByUserIdAndStatus(userId, RequestStatus.PENDING);
    }

    @Override
    public List<ProjectRequest> getPendingRequestsForManager(Integer managerId) {
        return requestRepository.findByProjectManagerIdAndStatus(managerId, RequestStatus.PENDING);
    }

    @Override
    public boolean hasActiveRequest(Integer userId, Integer projectId) {
        return requestRepository.existsByProjectIdAndUserIdAndStatus(projectId, userId, RequestStatus.PENDING);
    }
}