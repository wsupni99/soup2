package ru.itis.soup2.dto.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskLogDto {
    private Integer id;
    private String action;
    private LocalDateTime changedAt;
    private Integer userId;
    private String userName;
    private Integer taskId;
}