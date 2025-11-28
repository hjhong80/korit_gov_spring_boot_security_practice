package com.korit.security_practice.dto;

import com.korit.security_practice.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ModifyUsernameReqDto {
    private Integer userId;
    private String oldUsername;
    private String newUsername;

    public User toEntity() {
        return User.builder()
                .userId(userId)
                .username(newUsername)
                .build();
    }
}
