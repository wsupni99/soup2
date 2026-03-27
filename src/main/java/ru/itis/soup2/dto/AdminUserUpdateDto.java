package ru.itis.soup2.dto;

public record AdminUserUpdateDto(
        Integer id,
        String email,
        String name,
        String contactInfo,
        String roleName
) {}