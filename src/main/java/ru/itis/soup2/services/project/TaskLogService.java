package ru.itis.soup2.services.project;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.itis.soup2.dto.TaskLogDto;
import ru.itis.soup2.mappers.TaskLogMapper;
import ru.itis.soup2.repositories.project.TaskLogRepository;

@Service
@RequiredArgsConstructor
public class TaskLogService {

    private final TaskLogRepository taskLogRepository;
    private final TaskLogMapper taskLogMapper;

    public Page<TaskLogDto> findAllOrdered(Pageable pageable) {
        return taskLogRepository.findAllOrdered(pageable).map(taskLogMapper::toDto);
    }

    public Page<TaskLogDto> findByUserIdOptional(Integer userId, Pageable pageable) {
        return taskLogRepository.findByUserIdOptional(userId, pageable).map(taskLogMapper::toDto);
    }
}