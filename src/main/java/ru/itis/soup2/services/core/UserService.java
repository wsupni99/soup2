package ru.itis.soup2.services.core;

import ru.itis.soup2.dto.UserWithRoleDto;
import ru.itis.soup2.models.core.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void register(String email, String name, String rawPassword);
    Optional<User> login(String email, String rawPassword);
    List<User> getAllUsers();
    Optional<User> getUserById(Integer id);
    Optional<User> getByEmail(String email);
    List<User> getAllManagers();
    List<UserWithRoleDto> getUsersWithRoles();
}