package ru.itis.soup2.models.core;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "users", schema = "core")
public class User {
    @Id
    @Column(name = "user_id")
    private Integer userId;

    private String name;
    private String email;

    @Column(name = "password_hash")
    private String password;

    @Column(name = "contact_info")
    private String contactInfo;

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            schema = "core",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
}
