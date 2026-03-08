package ru.itis.soup2.dto;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class ProjectDto {
    private Integer id;
    private String name;
    private List<String> userEmails;
    private List<String> taskTitles;

    public ProjectDto(Integer id, String name, String users, String tasks) {
        this.id = id;
        this.name = name;
        this.userEmails = users != null ?
                Arrays.asList(users.split(",")) : List.of();
        this.taskTitles = tasks != null ?
                Arrays.asList(tasks.split(",")) : List.of();
    }
}