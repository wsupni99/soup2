package ru.itis.soup2.repositories.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.itis.soup2.models.core.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.password = :password")
    Optional<User> findByEmailAndPassword(
            @Param("email") String email,
            @Param("password") String password
    );

    List<User> findByRolesId(Integer roleId);

    @Query(value = """
    SELECT u.user_id, u.name, u.email, r.role_name 
    FROM core.users u 
    LEFT JOIN core.user_roles ur ON u.user_id = ur.user_id 
    LEFT JOIN core.roles r ON ur.role_id = r.role_id
    """, nativeQuery = true)
    List<Object[]> findUsersWithSingleRole();
}
