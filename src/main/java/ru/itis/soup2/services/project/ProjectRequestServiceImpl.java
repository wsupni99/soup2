package ru.itis.soup2.services.project;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.soup2.models.core.User;
import ru.itis.soup2.models.enums.RequestStatus;
import ru.itis.soup2.models.project.Project;
import ru.itis.soup2.models.project.ProjectRequest;
import ru.itis.soup2.repositories.core.UserRepository;
import ru.itis.soup2.repositories.project.ProjectRepository;
import ru.itis.soup2.repositories.project.ProjectRequestRepository;

import java.util.List;

@Slf4j
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
        try {
            log.info("Создание заявки на проект. ProjectId={}, UserId={}", projectId, userId);

            if (projectService.isUserInProject(userId, projectId)) {
                log.warn("Пользователь {} уже состоит в проекте {}", userId, projectId);
                throw new IllegalStateException("Вы уже являетесь участником этого проекта");
            }

            if (hasActiveRequest(userId, projectId)) {
                log.warn("У пользователя {} уже есть активная заявка на проект {}", userId, projectId);
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

            ProjectRequest saved = requestRepository.save(request);
            log.info("Заявка успешно создана. RequestId={}, ProjectId={}, UserId={}",
                    saved.getId(), projectId, userId);

            return saved;
        } catch (Exception e) {
            log.error("Ошибка при создании заявки на проект (projectId={}, userId={})", projectId, userId, e);
            throw e;
        }
    }

    @Transactional
    @Override
    public ProjectRequest approveRequest(Long requestId, Integer managerId) {
        try {
            log.info("Одобрение заявки ID: {} менеджером {}", requestId, managerId);

            ProjectRequest request = requestRepository.findById(requestId)
                    .orElseThrow(() -> new IllegalArgumentException("Заявка не найдена"));

            if (!request.getProject().getManager().getId().equals(managerId)) {
                log.warn("Менеджер {} не имеет прав на одобрение заявки {}", managerId, requestId);
                throw new IllegalStateException("У вас нет прав на одобрение этой заявки");
            }

            request.setStatus(RequestStatus.APPROVED);
            ProjectRequest saved = requestRepository.save(request);

            projectService.addUserToProject(request.getProject().getId(), request.getUser().getId());

            log.info("Заявка ID: {} успешно одобрена", requestId);
            return saved;
        } catch (Exception e) {
            log.error("Ошибка при одобрении заявки ID: {}", requestId, e);
            throw e;
        }
    }

    @Transactional
    @Override
    public ProjectRequest rejectRequest(Long requestId, Integer managerId) {
        try {
            log.info("Отклонение заявки ID: {} менеджером {}", requestId, managerId);

            ProjectRequest request = requestRepository.findById(requestId)
                    .orElseThrow(() -> new IllegalArgumentException("Заявка не найдена"));

            if (!request.getProject().getManager().getId().equals(managerId)) {
                log.warn("Менеджер {} не имеет прав на отклонение заявки {}", managerId, requestId);
                throw new IllegalStateException("У вас нет прав на отклонение этой заявки");
            }

            request.setStatus(RequestStatus.REJECTED);
            ProjectRequest saved = requestRepository.save(request);

            log.info("Заявка ID: {} успешно отклонена", requestId);
            return saved;
        } catch (Exception e) {
            log.error("Ошибка при отклонении заявки ID: {}", requestId, e);
            throw e;
        }
    }

    @Override
    public boolean hasActiveRequest(Integer userId, Integer projectId) {
        return requestRepository.existsByProjectIdAndUserIdAndStatus(projectId, userId, RequestStatus.PENDING);
    }

    @Override
    public List<ProjectRequest> getPendingRequestsForManager(Integer managerId) {
        return requestRepository.findByProjectManagerIdAndStatus(managerId, RequestStatus.PENDING);
    }

    @Override
    public List<ProjectRequest> getMyPendingRequests(Integer userId) {
        return requestRepository.findByUserIdAndStatus(userId, RequestStatus.PENDING);
    }
}