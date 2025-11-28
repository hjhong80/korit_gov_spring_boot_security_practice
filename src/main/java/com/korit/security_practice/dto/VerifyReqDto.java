package com.korit.security_practice.dto;

import com.korit.security_practice.entity.Verify;

public class VerifyReqDto {
    private Integer userId;
    private String verifyCode;

    public Verify toEntity() {
        return Verify.builder()
                .userId(userId)
                .verifyCode(verifyCode)
                .build();
    }
}
