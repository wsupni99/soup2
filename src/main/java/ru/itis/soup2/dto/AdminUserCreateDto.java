package ru.itis.soup2.dto;

public record AdminUserCreateDto(
        String email,
        String name,
        String password,
        String contactInfo,
        String roleName
) {}