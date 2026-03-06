package ru.itis.soup2.repositories.project;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.soup2.models.project.TaskLog;

public interface TaskLogRepository extends JpaRepository<TaskLog, Integer> {
}
