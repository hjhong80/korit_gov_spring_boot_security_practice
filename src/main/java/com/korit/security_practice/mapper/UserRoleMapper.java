package com.korit.security_practice.mapper;

import com.korit.security_practice.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRoleMapper {
    int addUserRole(UserRole userRole);
//    int userRegistry(UserRole userRole);
    int updateUserRole(UserRole userRole);
}
