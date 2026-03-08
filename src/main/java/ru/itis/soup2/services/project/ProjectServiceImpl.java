package ru.itis.soup2.services.project;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.soup2.models.project.Project;
import ru.itis.soup2.repositories.project.ProjectRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectsRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void create(Project project) {
        projectsRepository.save(project);
    }

    @Override
    public List<Project> getAllProjects() {
        return projectsRepository.findAll();
    }

    @Override
    public Optional<Project> getProjectById(Integer id) {
        return projectsRepository.findById(id);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void update(Project project) {
        projectsRepository.save(project);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void delete(Project project) {
        projectsRepository.delete(project);
    }
}
