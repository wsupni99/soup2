package ru.itis.soup2.repositories.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.itis.soup2.models.project.ProjectMember;

import java.util.List;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Integer> {

    @Query("SELECT pm FROM ProjectMember pm WHERE pm.project.id = :projectId")
    List<ProjectMember> findByProjectId(@Param("projectId") Integer projectId);

    @Query("SELECT pm FROM ProjectMember pm WHERE pm.user.id = :userId")
    List<ProjectMember> findByUserId(@Param("userId") Integer userId);

    void deleteByProjectIdAndUserIdIn(Integer projectId, List<Integer> userIds);
}