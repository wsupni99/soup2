package ru.itis.soup2.repositories.project;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.soup2.models.project.Project;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
}
