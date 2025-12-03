package com.korit.security_practice.repository;

import com.korit.security_practice.entity.Role;
import com.korit.security_practice.entity.UserRole;
import com.korit.security_practice.mapper.UserRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRoleRepository {
    private final UserRoleMapper userRoleMapper;

    public int addUserRole(UserRole userRole) {
        int result;
        try {
            result = userRoleMapper.addUserRole(userRole);
            System.out.println(result);
        } catch (RuntimeException e) {
            return 0;
        }
        return result;
    }

    public int updateUserRole(UserRole userRole) {
        return userRoleMapper.updateUserRole(userRole);
    }

//    public int userRegistry(UserRole userRole) {
//        int result;
//        return 0;
//    }
}
