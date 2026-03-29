package ru.itis.soup2.dto.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserUpdateDto {

    private Integer id;
    private String email;
    private String name;
    private String contactInfo;
    private String roleName;
}