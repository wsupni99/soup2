package ru.itis.soup2.repositories.project;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.soup2.models.project.ProjectMember;

import java.util.List;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Integer> {

    List<ProjectMember> findByProjectId(Integer projectId);

    void deleteByProjectIdAndUserIdIn(Integer projectId, List<Integer> userIds);
}