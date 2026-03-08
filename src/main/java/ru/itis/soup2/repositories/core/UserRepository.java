package ru.itis.soup2.repositories.core;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.soup2.models.core.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndPasswordHash(String email, String passwordHash);
}
