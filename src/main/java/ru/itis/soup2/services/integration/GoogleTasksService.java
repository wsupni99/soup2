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
            log.warn("Access token is missing for Google Tasks integration");
            return false;
        }

        try {
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
                log.info("Task '{}' successfully synced to Google Tasks", task.getName());
                return true;
            }
            return false;

        } catch (Exception e) {
            log.error("Error syncing task to Google Tasks: {}", e.getMessage());
            return false;
        }
    }
}