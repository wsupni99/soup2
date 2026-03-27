package ru.itis.soup2.services.project;

import ru.itis.soup2.models.project.Sprint;

import java.util.List;
import java.util.Optional;

public interface SprintService {

    void create(Sprint sprint);

    List<Sprint> getAllSprints();

    Optional<Sprint> getSprintById(Integer id);

    void update(Sprint sprint);

    void delete(Integer id);

    List<Sprint> findSprintsByProjectId(Integer projectId);

}