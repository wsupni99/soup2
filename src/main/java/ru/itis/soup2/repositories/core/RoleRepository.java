package ru.itis.soup2.repositories.core;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.soup2.models.core.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(String roleName);
}
