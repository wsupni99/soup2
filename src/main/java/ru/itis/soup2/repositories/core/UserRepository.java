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
}
