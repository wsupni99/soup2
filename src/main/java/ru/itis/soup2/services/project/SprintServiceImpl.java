package ru.itis.soup2.services.project;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.soup2.models.project.Sprint;
import ru.itis.soup2.repositories.project.SprintRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SprintServiceImpl implements SprintService {

    private final SprintRepository sprintRepository;

    @Transactional
    @Override
    public void create(Sprint sprint) {
        try {
            sprintRepository.save(sprint);
        } catch (Exception e) {
            log.error("Ошибка при создании спринта: {}", sprint.getName(), e);
            throw e;
        }
    }

    @Override
    public List<Sprint> getAllSprints() {
        return sprintRepository.findAll();
    }

    @Override
    public List<Sprint> findSprintsByProjectId(Integer projectId) {
        if (projectId == null) {
            return List.of();
        }
        return sprintRepository.findByProjectIdOrderByStartDate(projectId);
    }

    @Override
    public Optional<Sprint> getSprintById(Integer id) {
        return sprintRepository.findById(id);
    }

    @Transactional
    @Override
    public void update(Sprint sprint) {
        try {
            sprintRepository.save(sprint);
        } catch (Exception e) {
            log.error("Ошибка при обновлении спринта с id: {}", sprint.getId(), e);
            throw e;
        }
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        try {
            Sprint sprint = sprintRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Спринт не найден"));

            if (!sprint.getTasks().isEmpty()) {
                throw new IllegalStateException("Нельзя удалить спринт, в котором есть задачи");
            }

            sprintRepository.delete(sprint);
        } catch (Exception e) {
            log.error("Ошибка при удалении спринта с id: {}", id, e);
            throw e;
        }
    }
}