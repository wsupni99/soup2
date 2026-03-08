package ru.itis.soup2.services.core;

import ru.itis.soup2.models.core.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Optional<Role> findByName(String roleName);
    List<Role> findAll();
}
