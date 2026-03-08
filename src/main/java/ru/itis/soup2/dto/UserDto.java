package ru.itis.soup2.dto;

import java.util.List;

public record UserDto(
        Integer id,
        String name,
        String email,
        List<String> roles
) {}

