package ru.itis.soup2.repositories.project;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.soup2.models.project.Sprint;

public interface SprintRepository extends JpaRepository<Sprint, Integer> {
}
