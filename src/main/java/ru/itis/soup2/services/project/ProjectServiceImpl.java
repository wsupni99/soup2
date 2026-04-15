package ru.itis.soup2.services.project;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.soup2.models.core.User;
import ru.itis.soup2.models.enums.ProjectMemberRole;
import ru.itis.soup2.models.project.Project;
import ru.itis.soup2.models.project.ProjectMember;
import ru.itis.soup2.repositories.core.UserRepository;
import ru.itis.soup2.repositories.project.ProjectMemberRepository;
import ru.itis.soup2.repositories.project.ProjectRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;


    @Transactional
    @Override
    public void create(Project project) {
        projectRepository.save(project);
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public Optional<Project> getProjectById(Integer id) {
        return projectRepository.findById(id);
    }

    @Transactional
    @Override
    public void update(Project project) {
        projectRepository.save(project);
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Проект не найден"));
        if (!project.getSprints().isEmpty()) {
            throw new IllegalStateException("Нельзя удалить проект с привязанными спринтами");
        }
        if (!project.getTasks().isEmpty()) {
            throw new IllegalStateException("Нельзя удалить проект с привязанными задачами");
        }
        projectRepository.delete(project);
    }

    @Transactional
    @Override
    public void updateMembers(Integer projectId, List<Integer> memberIds) {
        List<Integer> finalMemberIds = (memberIds == null) ? List.of() : memberIds;

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        List<ProjectMember> current = projectMemberRepository.findByProjectId(projectId);
        List<Integer> currentIds = current.stream()
                .map(m -> m.getUser().getId())
                .toList();

        List<Integer> toDelete = currentIds.stream()
                .filter(id -> !finalMemberIds.contains(id))
                .toList();

        List<Integer> toAdd = finalMemberIds.stream()
                .filter(id -> !currentIds.contains(id))
                .toList();

        if (!toDelete.isEmpty()) {
            projectMemberRepository.deleteByProjectIdAndUserIdIn(projectId, toDelete);
        }
        for (Integer userId : toAdd) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));

            ProjectMember member = ProjectMember.builder()
                    .project(project)
                    .user(user)
                    .roleInProject(ProjectMemberRole.DEVELOPER)
                    .joinedAt(LocalDate.now())
                    .build();

            projectMemberRepository.save(member);
        }
    }
}