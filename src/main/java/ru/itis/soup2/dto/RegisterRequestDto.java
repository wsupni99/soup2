package ru.itis.soup2.dto;

public record RegisterRequestDto(
        String name,
        String email,
        String password,
        String roleName
) {}