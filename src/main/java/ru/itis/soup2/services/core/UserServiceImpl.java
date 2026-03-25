package ru.itis.soup2.services.core;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    public void register(String email, String name, String rawPassword) {
        log.info("=== РЕГИСТРАЦИЯ === Попытка регистрации пользователя: {}", email);

        User user = new User();
        user.setEmail(email.trim());
        user.setName(name.trim());
        user.setContactInfo("");

        String encodedPassword = passwordEncoder.encode(rawPassword.trim());
        user.setPassword(encodedPassword);

        log.info("Пароль зашифрован. Хэш начинается с: {}", encodedPassword.substring(0, 20) + "...");

        userRepository.save(user);
        log.info("Пользователь успешно сохранён в БД с id = {}", user.getId());
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
        Role managerRole = roleService.findByName(MANAGER_ROLE_NAME)
                .orElseThrow(() -> new EntityNotFoundException("Role MANAGER not found"));

        return userRepository.findByRolesId(managerRole.getId());
    }

    @Override
    public List<UserWithRoleDto> getUsersWithRoles() {
        return userRepository.findUsersWithRoles();
    }
}