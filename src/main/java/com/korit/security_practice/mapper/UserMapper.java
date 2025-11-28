package com.korit.security_practice.mapper;

import com.korit.security_practice.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {
    Optional<User> findById(Integer userId);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    List<User> findAll();
    int addUser(User user);
    int modifyUsername(User user);
    int modifyPassword(User user);
}
