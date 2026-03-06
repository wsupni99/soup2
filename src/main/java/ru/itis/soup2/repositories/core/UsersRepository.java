package ru.itis.soup2.repositories.core;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.soup2.models.core.User;

public interface UsersRepository extends JpaRepository<User, Integer> {
}
