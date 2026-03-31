package ru.itis.soup2.services.core;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.soup2.dto.core.AdminUserCreateDto;
import ru.itis.soup2.dto.core.AdminUserUpdateDto;
import ru.itis.soup2.dto.core.RegisterRequestDto;
import ru.itis.soup2.models.core.Role;
import ru.itis.soup2.models.core.User;
import ru.itis.soup2.repositories.core.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Transactional
    @Override
    public void register(RegisterRequestDto dto) {
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
    }

    @Transactional
    @Override
    public void createUser(AdminUserCreateDto dto) {
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
    }

    @Transactional
    @Override
    public void updateUser(AdminUserUpdateDto dto) {
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
        }

        userRepository.save(user);
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