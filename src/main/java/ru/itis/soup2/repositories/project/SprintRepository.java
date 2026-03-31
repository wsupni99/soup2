package ru.itis.soup2.repositories.project;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.soup2.models.project.Sprint;

import java.util.List;
import java.util.Optional;

public interface SprintRepository extends JpaRepository<Sprint, Integer> {
    @EntityGraph(attributePaths = {"project", "tasks"})
    List<Sprint> findAll();

    @EntityGraph(attributePaths = {"project", "tasks"})
    Optional<Sprint> findById(Integer id);

    List<Sprint> findByProjectIdOrderByStartDate(Integer projectId);
}