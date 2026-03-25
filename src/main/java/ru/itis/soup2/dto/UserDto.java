package ru.itis.soup2.dto;

import java.util.List;

public record UserDto(
        Integer id,
        String email,
        String name,
        String contactInfo,
        List<String> roles
) {}
