package ru.itis.soup2.repositories.core;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.soup2.models.core.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
