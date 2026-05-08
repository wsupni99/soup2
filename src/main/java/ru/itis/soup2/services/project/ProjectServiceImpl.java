package ru.itis.soup2.services.project;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
        try {
            log.info("Создание проекта: {}", project.getName());
            projectRepository.save(project);
            log.info("Проект успешно создан. ID: {}", project.getId());
        } catch (Exception e) {
            log.error("Ошибка при создании проекта: {}", project.getName(), e);
            throw e;
        }
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
        try {
            log.info("Обновление проекта ID: {}", project.getId());
            projectRepository.save(project);
            log.info("Проект ID: {} успешно обновлён", project.getId());
        } catch (Exception e) {
            log.error("Ошибка при обновлении проекта с id: {}", project.getId(), e);
            throw e;
        }
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        try {
            log.info("Попытка удаления проекта ID: {}", id);

            Project project = projectRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Проект не найден"));

            if (!project.getSprints().isEmpty()) {
                log.warn("Невозможно удалить проект ID: {} — есть привязанные спринты", id);
                throw new IllegalStateException("Нельзя удалить проект с привязанными спринтами");
            }

            if (!project.getTasks().isEmpty()) {
                log.warn("Невозможно удалить проект ID: {} — есть привязанные задачи", id);
                throw new IllegalStateException("Нельзя удалить проект с привязанными задачами");
            }

            projectRepository.delete(project);
            log.info("Проект ID: {} успешно удалён", id);

        } catch (Exception e) {
            log.error("Ошибка при удалении проекта с id: {}", id, e);
            throw e;
        }
    }

    @Transactional
    @Override
    public void updateMembers(Integer projectId, List<Integer> memberIds) {
        try {
            List<Integer> finalMemberIds = (memberIds == null) ? List.of() : memberIds;

            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new EntityNotFoundException("Project not found"));

            List<ProjectMember> current = projectMemberRepository.findAllByProjectId(projectId);
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
        } catch (Exception e) {
            log.error("Ошибка при обновлении участников проекта с id: {}. Причина: {}", projectId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<Project> getProjectsByUserId(Integer userId) {
        return projectMemberRepository.findAllByUserId(userId)
                .stream()
                .map(ProjectMember::getProject)
                .toList();
    }

    @Override
    public boolean isUserInProject(Integer userId, Integer projectId) {
        return projectMemberRepository.existsByProjectIdAndUserId(projectId, userId);
    }

    @Transactional
    @Override
    public void addUserToProject(Integer projectId, Integer userId) {
        try {
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new EntityNotFoundException("Проект не найден"));

            if (isUserInProject(userId, projectId)) {
                log.warn("Пользователь {} уже является участником проекта {}", userId, projectId);
                return;
            }

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

            ProjectMember member = ProjectMember.builder()
                    .project(project)
                    .user(user)
                    .roleInProject(ProjectMemberRole.DEVELOPER)
                    .joinedAt(LocalDate.now())
                    .build();

            projectMemberRepository.save(member);

            log.info("Пользователь {} успешно добавлен в проект {} как DEVELOPER", userId, projectId);

        } catch (Exception e) {
            log.error("Ошибка при добавлении пользователя {} в проект {}: {}",
                    userId, projectId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<Project> getAvailableProjectsForUser(Integer userId) {
        List<Project> allProjects = projectRepository.findAll();

        return allProjects.stream()
                .filter(project -> !isUserInProject(userId, project.getId()))
                .toList();
    }
}