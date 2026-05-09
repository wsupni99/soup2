package ru.itis.soup2.repositories.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.itis.soup2.models.project.ProjectRequest;
import ru.itis.soup2.models.enums.RequestStatus;

import java.util.List;
import java.util.Optional;

public interface ProjectRequestRepository extends JpaRepository<ProjectRequest, Long> {

    List<ProjectRequest> findByUserIdAndStatus(Integer userId, RequestStatus status);

    Optional<ProjectRequest> findByProjectIdAndUserIdAndStatus(Integer projectId, Integer userId, RequestStatus status);

    boolean existsByProjectIdAndUserIdAndStatus(Integer projectId, Integer userId, RequestStatus status);

    @Query("SELECT pr FROM ProjectRequest pr " +
            "JOIN pr.project p " +
            "WHERE p.manager.id = :managerId AND pr.status = :status")
    List<ProjectRequest> findByProjectManagerIdAndStatus(
            @Param("managerId") Integer managerId,
            @Param("status") RequestStatus status);
}