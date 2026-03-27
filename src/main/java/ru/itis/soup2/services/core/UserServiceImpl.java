package ru.itis.soup2.services.core;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.soup2.dto.RegisterRequestDto;
import ru.itis.soup2.dto.UserWithRoleDto;
import ru.itis.soup2.models.core.Role;
import ru.itis.soup2.models.core.User;
import ru.itis.soup2.repositories.core.UserRepository;

import java.util.List;
import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)   // по умолчанию все методы только чтение
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    private static final String MANAGER_ROLE_NAME = "MANAGER";

    @Transactional
    @Override
    public void register(RegisterRequestDto dto) {
        log.info("Регистрация пользователя: email={}, роль={}", dto.email(), dto.roleName());

        User user = new User();
        user.setEmail(dto.email().trim());
        user.setName(dto.name().trim());
        user.setPassword(passwordEncoder.encode(dto.password().trim()));
        user.setContactInfo("");

        User savedUser = userRepository.save(user);

        // Определяем роль
        String roleName = dto.roleName() != null ? dto.roleName().trim().toUpperCase() : "ROLE_DEVELOPER";

        if (!roleName.startsWith("ROLE_")) {
            roleName = "ROLE_" + roleName;
        }

        Role role = roleService.findByName(roleName)
                .orElseGet(() -> roleService.findByName("ROLE_DEVELOPER")
                        .orElseThrow(() -> new EntityNotFoundException("Default role not found")));

        savedUser.setRole(role);           // ← новая связь!
        userRepository.save(savedUser);

        log.info("Пользователь {} успешно зарегистрирован с ролью {}", dto.email(), roleName);
    }

    @Override
    public Optional<User> login(String email, String rawPassword) {
        log.info("=== ЛОГИН === Попытка входа для email: {}", email);

        Optional<User> userOpt = userRepository.findByEmail(email.trim());

        if (userOpt.isEmpty()) {
            log.warn("Пользователь с email {} не найден в БД", email);
            return Optional.empty();
        }

        User user = userOpt.get();
        log.info("Пользователь найден. ID = {}, Name = {}", user.getId(), user.getName());

        boolean passwordMatches = passwordEncoder.matches(rawPassword.trim(), user.getPassword());

        log.info("Сравнение паролей:");
        log.info("  Введённый пароль: {}", rawPassword.trim());
        log.info("  Хэш из БД: {}", user.getPassword());
        log.info("  Результат matches(): {}", passwordMatches);

        if (passwordMatches) {
            log.info("Логин УСПЕШНЫЙ для пользователя {}", email);
            return Optional.of(user);
        } else {
            log.warn("Логин НЕУДАЧНЫЙ — пароль не совпал");
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAllManagers() {
        return userRepository.findManagers();
    }

    @Override
    public List<UserWithRoleDto> getUsersWithRoles() {
        return userRepository.findUsersWithRoles();
    }
}