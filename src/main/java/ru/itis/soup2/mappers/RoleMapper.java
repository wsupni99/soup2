package ru.itis.soup2.mappers;

import ru.itis.soup2.dto.RoleDto;
import ru.itis.soup2.models.core.Role;
import java.util.List;
import java.util.stream.Collectors;

public final class RoleMapper {

    private RoleMapper() {}

    public static RoleDto toDto(Role role) {
        if (role == null) return null;
        return new RoleDto(
                role.getId(),
                role.getRoleName(),
                role.getDescription()
        );
    }

    public static List<RoleDto> toDtoList(List<Role> roles) {
        return roles.stream()
                .map(RoleMapper::toDto)
                .collect(Collectors.toList());
    }
}