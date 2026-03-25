package ru.itis.soup2.repositories.project;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.soup2.models.project.Sprint;

import java.util.List;

public interface SprintRepository extends JpaRepository<Sprint, Integer> {

    List<Sprint> findByProjectId(Integer projectId);

    long countByProjectId(Integer projectId);

    List<Sprint> findByProjectIdOrderByStartDateAsc(Integer projectId);
}