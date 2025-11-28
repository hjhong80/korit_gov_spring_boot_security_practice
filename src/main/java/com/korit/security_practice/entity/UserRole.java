package com.korit.security_practice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {
    private Integer userRoleId;
    private Integer userId;
    private Integer roleId;

    private Role role;
}
