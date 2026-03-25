package ru.itis.soup2.repositories.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.itis.soup2.dto.UserWithRoleDto;
import ru.itis.soup2.models.core.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    @Query("""
        SELECT new ru.itis.soup2.dto.UserWithRoleDto(
            u.id,
            u.name,
            u.email,
            r.roleName
        )
        FROM User u
        LEFT JOIN u.role r
        """)
    List<UserWithRoleDto> findUsersWithRoles();

    @Query("SELECT u FROM User u WHERE u.role.roleName = :roleName")
    List<User> findByRoleName(String roleName);

    @Query("SELECT u FROM User u WHERE u.role.roleName = 'ROLE_MANAGER'")
    List<User> findManagers();
}