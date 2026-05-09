package ru.itis.soup2.repositories.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.itis.soup2.models.project.Project;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    @Query("SELECT DISTINCT p FROM Project p " +
            "LEFT JOIN p.members m " +
            "WHERE p.manager.id = :userId OR m.user.id = :userId")
    List<Project> findProjectsByUserId(@Param("userId") Integer userId);

    @Query("SELECT p FROM Project p " +
            "WHERE p.id NOT IN (" +
            "  SELECT DISTINCT p2.id FROM Project p2 " +
            "  LEFT JOIN p2.members m " +
            "  WHERE p2.manager.id = :userId OR m.user.id = :userId" +
            ")")
    List<Project> findAvailableProjectsForUser(@Param("userId") Integer userId);
}