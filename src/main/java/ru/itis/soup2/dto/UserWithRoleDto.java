package ru.itis.soup2.dto;

public record UserWithRoleDto(
        Integer userId,
        String name,
        String email,
        String roleName
) {}