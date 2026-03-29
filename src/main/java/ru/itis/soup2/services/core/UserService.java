package ru.itis.soup2.services.core;

import ru.itis.soup2.dto.core.AdminUserCreateDto;
import ru.itis.soup2.dto.core.AdminUserUpdateDto;
import ru.itis.soup2.dto.core.RegisterRequestDto;
import ru.itis.soup2.dto.core.UserWithRoleDto;
import ru.itis.soup2.models.core.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void register(RegisterRequestDto dto);
    Optional<User> login(String email, String rawPassword);
    void updateUser(User user);

    List<User> getAllUsers();
    Optional<User> getUserById(Integer id);
    Optional<User> getByEmail(String email);
    List<User> getAllManagers();
    List<UserWithRoleDto> getUsersWithRoles();
    void createUser(AdminUserCreateDto dto);
    void updateUser(AdminUserUpdateDto dto);
    void deleteUser(Integer id);
}