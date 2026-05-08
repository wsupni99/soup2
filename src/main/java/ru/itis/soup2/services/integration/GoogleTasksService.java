package ru.itis.soup2.services.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.itis.soup2.models.project.Task;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleTasksService {

    private final RestTemplate restTemplate;
    private static final String GOOGLE_TASKS_API_URL = "https://tasks.googleapis.com/tasks/v1/lists/@default/tasks";

    public boolean addTaskToGoogleTasks(Task task, String accessToken) {
        if (accessToken == null) {
            log.warn("Попытка синхронизации задачи в Google Tasks без access token. Task: {}", task.getName());
            return false;
        }

        try {
            log.info("Отправка задачи '{}' в Google Tasks", task.getName());

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> body = new HashMap<>();
            body.put("title", task.getName());
            body.put("notes", task.getDescription() != null ? task.getDescription() : "");

            if (task.getDeadline() != null) {
                String formattedDate = task.getDeadline()
                        .atStartOfDay(ZoneId.of("UTC"))
                        .format(DateTimeFormatter.ISO_INSTANT);
                body.put("due", formattedDate);
            }

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.exchange(GOOGLE_TASKS_API_URL, HttpMethod.POST, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Задача '{}' успешно синхронизирована в Google Tasks", task.getName());
                return true;
            } else {
                log.warn("Google Tasks вернул статус {} при синхронизации задачи '{}'",
                        response.getStatusCode(), task.getName());
                return false;
            }

        } catch (Exception e) {
            log.error("Ошибка синхронизации задачи '{}' в Google Tasks", task.getName(), e);
            return false;
        }
    }
}