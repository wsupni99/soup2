package ru.itis.soup2.services.project;

import ru.itis.soup2.models.project.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectService {
    void create(Project project);
    List<Project> getAllProjects();
    Optional<Project> getProjectById(Integer id);
    void update(Project project);
    void delete(Project project);
}
