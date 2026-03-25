package ru.itis.soup2.mappers;

import ru.itis.soup2.dto.UserDto;
import ru.itis.soup2.models.core.User;
import java.util.List;
import java.util.stream.Collectors;

public final class UserMapper {

    private UserMapper() {}

    public static UserDto toDto(User user) {
        if (user == null) return null;

        String roleName = (user.getRole() != null) ? user.getRole().getRoleName() : null;

        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getContactInfo(),
                user.getRole()
        );
    }

    public static List<UserDto> toDtoList(List<User> users) {
        return users.stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    public static User toEntity(UserDto dto) {
        if (dto == null) return null;
        User user = new User();
        user.setEmail(dto.email());
        user.setName(dto.name());
        user.setContactInfo(dto.contactInfo());
        return user;
    }
}