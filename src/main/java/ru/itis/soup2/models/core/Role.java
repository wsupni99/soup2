package ru.itis.soup2.models.core;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "roles", schema = "core")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="role_id")
    private Integer id;

    @Column(name="role_name")
    private String roleName;

    private String description;

    @OneToMany(mappedBy = "role")
    private List<User> users = new ArrayList<>();
}
