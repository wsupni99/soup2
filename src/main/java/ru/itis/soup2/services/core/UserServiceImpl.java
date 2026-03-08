package ru.itis.soup2.services.core;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.soup2.dto.UserDto;
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
    private static final String MANAGER_ROLE_NAME = "MANAGER";

    @Override
    public void register(String email, String name, String rawPassword) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setContactInfo("");
        userRepository.save(user);
    }

    @Override
    public Optional<User> login(String email, String rawPassword) {
        return userRepository.findByEmail(email.trim())
                .filter(user -> passwordEncoder.matches(rawPassword.trim(), user.getPassword()));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    @Transactional
    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAllManagers() {
        Role managerRole = roleService.findByName(MANAGER_ROLE_NAME)
                .orElseThrow(()-> new EntityNotFoundException("Role MANAGER not found"));
        return userRepository.findByRolesId(managerRole.getId());
    }

    @Override
    public List<UserDto> getUsersDto() {
        return userRepository.findUsersWithSingleRole().stream()
                .map(row -> new UserDto(
                        (Integer) row[0],
                        (String) row[1],
                        (String) row[2],
                        row[3] != null ? List.of((String) row[3]) : List.of()
                ))
                .toList();
    }
}
