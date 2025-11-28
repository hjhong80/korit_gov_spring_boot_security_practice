package com.korit.security_practice.repository;

import com.korit.security_practice.entity.User;
import com.korit.security_practice.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final UserMapper userMapper;
    private final UserRoleRepository userRoleRepository;

    public Optional<User> findById(Integer userId) {
        System.out.println("UserRepository : findById");
        return userMapper.findById(userId);
    }

    public Optional<User> findByEmail(String email) {
        System.out.println("UserRepository : findByEmail");
        return userMapper.findByEmail(email);
    }

    public Optional<User> findByUsername(String username) {
        System.out.println("UserRepository : findByUsername");
        return userMapper.findByUsername(username);
    }

    public Optional<User> addUser(User user) {
        System.out.println("UserRepository : addUser");
        try {
            int result = userMapper.addUser(user);
            System.out.println(result);
        } catch (DuplicateKeyException e) {
            return Optional.empty();
        }
        return Optional.of(user);
    }

    public List<User> findAll() {
        System.out.println("UserRepository : findAll");
        return userMapper.findAll();
    }

    public int modifyUsername(User user) {
        System.out.println("UserRepository : modifyUsername");
        return userMapper.modifyUsername(user);
    }

    public int modifyPassword(User user) {
        System.out.println("UserRepository : modifyPassword");
        return userMapper.modifyPassword(user);
    }

}
