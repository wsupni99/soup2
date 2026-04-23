package ru.itis.soup2.services.project;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.itis.soup2.dto.project.TaskLogDto;
import ru.itis.soup2.mappers.project.TaskLogMapper;
import ru.itis.soup2.repositories.project.TaskLogRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskLogService {

    private final TaskLogRepository taskLogRepository;
    private final TaskLogMapper taskLogMapper;

    public Page<TaskLogDto> findAllOrdered(Pageable pageable) {
        try {
            return taskLogRepository.findAllOrdered(pageable).map(taskLogMapper::toDto);
        } catch (Exception e) {
            log.error("Ошибка при получении логов задач", e);
            throw e;
        }
    }

    public Page<TaskLogDto> findByUserIdOptional(Integer userId, Pageable pageable) {
        try {
            return taskLogRepository.findByUserIdOptional(userId, pageable).map(taskLogMapper::toDto);
        } catch (Exception e) {
            log.error("Ошибка при получении логов задач для пользователя с id: {}", userId, e);
            throw e;
        }
    }
}