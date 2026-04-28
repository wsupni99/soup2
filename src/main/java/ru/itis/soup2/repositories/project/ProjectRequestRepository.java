package ru.itis.soup2.repositories.project;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.soup2.models.project.ProjectRequest;
import ru.itis.soup2.models.enums.RequestStatus;

import java.util.List;
import java.util.Optional;

public interface ProjectRequestRepository extends JpaRepository<ProjectRequest, Long> {

    List<ProjectRequest> findByUserIdAndStatus(Integer userId, RequestStatus status);

    List<ProjectRequest> findByProjectManagerIdAndStatus(Integer managerId, RequestStatus status);

    Optional<ProjectRequest> findByProjectIdAndUserIdAndStatus(Integer projectId, Integer userId, RequestStatus status);

    boolean existsByProjectIdAndUserIdAndStatus(Integer projectId, Integer userId, RequestStatus status);
}