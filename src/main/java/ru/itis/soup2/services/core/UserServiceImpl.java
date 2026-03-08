package ru.itis.soup2.services.core;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        return userRepository.findByRolesId(2);
    }
}
