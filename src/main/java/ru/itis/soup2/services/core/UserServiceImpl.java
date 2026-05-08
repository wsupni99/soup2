package ru.itis.soup2.services.core;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.soup2.dto.core.AdminUserCreateDto;
import ru.itis.soup2.dto.core.AdminUserUpdateDto;
import ru.itis.soup2.dto.core.RegisterRequestDto;
import ru.itis.soup2.models.core.Role;
import ru.itis.soup2.models.core.User;
import ru.itis.soup2.repositories.core.RoleRepository;
import ru.itis.soup2.repositories.core.UserRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final RoleRepository roleRepository;

    @Transactional
    @Override
    public void register(RegisterRequestDto dto) {
        try {
            log.info("Регистрация пользователя: {}", dto.getEmail());

            User user = new User();
            user.setEmail(dto.getEmail().trim());
            user.setName(dto.getName().trim());
            user.setPassword(passwordEncoder.encode(dto.getPassword().trim()));
            user.setContactInfo("");

            User savedUser = userRepository.save(user);

            String roleName = dto.getRoleName() != null
                    ? dto.getRoleName().trim().toUpperCase()
                    : "ROLE_DEVELOPER";

            if (!roleName.startsWith("ROLE_")) {
                roleName = "ROLE_" + roleName;
            }

            Role role = roleService.findByName(roleName)
                    .orElseGet(() -> roleService.findByName("ROLE_DEVELOPER")
                            .orElseThrow(() -> new EntityNotFoundException("Default role not found")));

            savedUser.setRole(role);
            userRepository.save(savedUser);

            log.info("Пользователь успешно зарегистрирован. Email: {}, Role: {}", dto.getEmail(), roleName);
        } catch (Exception e) {
            log.error("Ошибка при регистрации пользователя с email: {}", dto.getEmail(), e);
            throw e;
        }
    }

    @Transactional
    @Override
    public void createUser(AdminUserCreateDto dto) {
        try {
            log.info("Создание пользователя администратором. Email: {}", dto.getEmail());

            User user = new User();
            user.setEmail(dto.getEmail().trim());
            user.setName(dto.getName().trim());
            user.setPassword(passwordEncoder.encode(dto.getPassword().trim()));
            user.setContactInfo(dto.getContactInfo() != null ? dto.getContactInfo().trim() : "");

            User savedUser = userRepository.save(user);

            String roleName = normalizeRoleName(dto.getRoleName());
            Role role = roleService.findByName(roleName)
                    .orElseGet(() -> roleService.findByName("ROLE_DEVELOPER")
                            .orElseThrow(() -> new EntityNotFoundException("Default role ROLE_DEVELOPER not found")));

            savedUser.setRole(role);
            userRepository.save(savedUser);

            log.info("Пользователь успешно создан администратором. Email: {}, Role: {}", dto.getEmail(), roleName);
        } catch (Exception e) {
            log.error("Ошибка при создании пользователя админом: {}", dto.getEmail(), e);
            throw e;
        }
    }

    @Transactional
    @Override
    public void updateUser(AdminUserUpdateDto dto) {
        try {
            log.info("Обновление пользователя ID: {}", dto.getId());

            User user = userRepository.findById(dto.getId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + dto.getId()));

            user.setEmail(dto.getEmail().trim());
            user.setName(dto.getName().trim());
            user.setContactInfo(dto.getContactInfo() != null ? dto.getContactInfo().trim() : "");

            if (dto.getRoleName() != null && !dto.getRoleName().isBlank()) {
                String roleName = normalizeRoleName(dto.getRoleName());
                Role newRole = roleService.findByName(roleName)
                        .orElseThrow(() -> new EntityNotFoundException("Role not found: " + roleName));

                user.setRole(newRole);
                log.info("У пользователя ID: {} изменена роль на {}", dto.getId(), roleName);
            }

            userRepository.save(user);
            log.info("Пользователь ID: {} успешно обновлён", dto.getId());
        } catch (Exception e) {
            log.error("Ошибка при обновлении пользователя с id: {}", dto.getId(), e);
            throw e;
        }
    }

    @Override
    public User findOrCreateOAuthUser(String email, String fullName) {
        try {
            log.info("Поиск/создание OAuth пользователя: {}", email);

            return userRepository.findByEmail(email)
                    .orElseGet(() -> {
                        Role defaultRole = roleRepository.findByRoleName("ROLE_DEVELOPER")
                                .orElseGet(() -> roleRepository.findByRoleName("ROLE_USER")
                                        .orElseThrow(() -> new RuntimeException("Default role not found")));

                        User newUser = User.builder()
                                .email(email)
                                .name(fullName != null ? fullName : "OAuth User")
                                .password("")
                                .oauthProvider("google")
                                .role(defaultRole)
                                .contactInfo("")
                                .build();

                        User saved = userRepository.save(newUser);
                        log.info("Создан новый OAuth пользователь: {}", email);
                        return saved;
                    });
        } catch (Exception e) {
            log.error("Ошибка при findOrCreateOAuthUser для email: {}", email, e);
            throw e;
        }
    }

    private String normalizeRoleName(String roleName) {
        if (roleName == null) return "ROLE_DEVELOPER";
        String upper = roleName.trim().toUpperCase();
        return upper.startsWith("ROLE_") ? upper : "ROLE_" + upper;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }
}