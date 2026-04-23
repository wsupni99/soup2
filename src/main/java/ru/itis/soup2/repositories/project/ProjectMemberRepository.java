package ru.itis.soup2.repositories.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.itis.soup2.models.project.ProjectMember;

import java.util.List;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Integer> {
    List<ProjectMember> findAllByProjectId(Integer projectId);

    void deleteByProjectIdAndUserIdIn(Integer projectId, List<Integer> userIds);
}