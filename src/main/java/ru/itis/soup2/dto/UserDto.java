package ru.itis.soup2.dto;

import ru.itis.soup2.models.core.Role;

public record UserDto(
        Integer id,
        String email,
        String name,
        String contactInfo,
        Role role
) {}
