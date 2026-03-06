package ru.itis.soup2.services;

import org.springframework.stereotype.Component;
import ru.itis.soup2.models.project.Project;
import ru.itis.soup2.repositories.project.ProjectRepository;

@Component
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectsRepository;

    public ProjectServiceImpl(ProjectRepository projectsRepository) {
        this.projectsRepository = projectsRepository;
    }

    @Override
    public void create(Project project) {
        projectsRepository.save(project);
    }
}
