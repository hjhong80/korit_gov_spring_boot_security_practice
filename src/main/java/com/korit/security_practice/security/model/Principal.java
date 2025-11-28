package com.korit.security_practice.security.model;

import com.korit.security_practice.entity.UserRole;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class Principal {
    private Integer userId;
    private String username;
    private String password;
    private String email;

    private List<UserRole> userRoleList;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println("Principal : getAuthorities");
        return userRoleList.stream()
                .map(userRole -> new SimpleGrantedAuthority(userRole.getRole().getRoleName()))
                .collect(Collectors.toList());
    }

}
