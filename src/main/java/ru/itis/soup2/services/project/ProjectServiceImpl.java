package ru.itis.soup2.services.project;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.soup2.models.project.Project;
import ru.itis.soup2.repositories.project.ProjectRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

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
}