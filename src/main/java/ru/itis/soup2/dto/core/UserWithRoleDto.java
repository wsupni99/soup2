package ru.itis.soup2.dto.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserWithRoleDto {

    private Integer userId;
    private String name;
    private String email;
    private String roleName;
}