package com.korit.security_practice.repository;

import com.korit.security_practice.entity.Verify;
import com.korit.security_practice.mapper.VerifyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VerifyRepository {
    public final VerifyMapper verifyMapper;
    public int addVerify(Verify verify) {
        return verifyMapper.addVerify(verify);
    }

    public int deleteVerify(Verify verify) {
        return verifyMapper.deleteVerify(verify);
    }

    public Optional<Verify> findVerify(Integer userId) {
        return verifyMapper.findVerify(userId);
    }
}
