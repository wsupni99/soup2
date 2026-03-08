package ru.itis.soup2.repositories.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.itis.soup2.models.enums.ProjectStatus;
import ru.itis.soup2.models.project.Project;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    @Query("SELECT p FROM Project p WHERE p.manager.id = :managerId")
    List<Project> findByManagerId(@Param("managerId") Integer managerId);

    List<Project> findByStatus(ProjectStatus status);
}