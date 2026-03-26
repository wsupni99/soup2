package ru.itis.soup2.services.project;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.soup2.models.project.Sprint;
import ru.itis.soup2.repositories.project.SprintRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SprintServiceImpl implements SprintService {

    private final SprintRepository sprintRepository;

    @Transactional
    @Override
    public void create(Sprint sprint) {
        sprintRepository.save(sprint);
    }

    // Основной метод для списка всех спринтов (с проектом и задачами)
    @Override
    public List<Sprint> getAllSprints() {
        return sprintRepository.findAll();                    // теперь используем переопределённый findAll()
    }

    @Override
    public List<Sprint> getSprintsByProjectId(Integer projectId) {
        return sprintRepository.findByProjectId(projectId);   // с EntityGraph
    }

    // Основной метод для получения одного спринта (используется в edit и update)
    @Override
    public Optional<Sprint> getSprintById(Integer id) {
        return sprintRepository.findById(id);                 // с EntityGraph
    }

    @Transactional
    @Override
    public void update(Sprint sprint) {
        sprintRepository.save(sprint);
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        // Используем метод с подгрузкой задач, чтобы проверить наличие задач
        Sprint sprint = sprintRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sprint not found"));

        if (!sprint.getTasks().isEmpty()) {
            throw new IllegalStateException("Cannot delete sprint with existing tasks");
        }

        sprintRepository.delete(sprint);
    }
}