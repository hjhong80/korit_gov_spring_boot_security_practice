package com.korit.security_practice.mapper;

import com.korit.security_practice.dto.VerifyReqDto;
import com.korit.security_practice.entity.Verify;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface VerifyMapper {
    int addVerify(Verify verify);
    int deleteVerify(Verify verify);
    Optional<Verify> findVerify(Integer userId);
}
