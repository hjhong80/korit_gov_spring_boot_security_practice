package com.korit.security_practice.dto;

import com.korit.security_practice.entity.Verify;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerifyReqDto {
    private String verifyCode;
}
