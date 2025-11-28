package com.korit.security_practice.service;

import com.korit.security_practice.dto.ApiRespDto;
import com.korit.security_practice.dto.ModifyPasswordReqDto;
import com.korit.security_practice.dto.ModifyUsernameReqDto;
import com.korit.security_practice.dto.VerifyReqDto;
import com.korit.security_practice.entity.User;
import com.korit.security_practice.entity.Verify;
import com.korit.security_practice.repository.UserRepository;
import com.korit.security_practice.repository.VerifyRepository;
import com.korit.security_practice.security.model.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final UserRepository userRepository;
    private final VerifyRepository verifyRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public ApiRespDto<?> modifyUsername(ModifyUsernameReqDto modifyUsernameReqDto, Principal principal) {
        System.out.println("AccountService : modifyUsername");
        System.out.println(modifyUsernameReqDto);
        if(modifyUsernameReqDto.getOldUsername().isEmpty() || modifyUsernameReqDto.getNewUsername().isEmpty()) {
            System.out.println("modifyUsername : username 없음");
            return new ApiRespDto<>("failed", "username은 공백일 수 없습니다.", null);
        }
        if(!modifyUsernameReqDto.getUserId().equals(principal.getUserId())) {
            System.out.println("modifyUsername : 인증 이상 - userId 불일치");
            return new ApiRespDto<>("failed", "잘못된 접근 입니다.", null);
        }

        Optional<User> foundUser = userRepository.findById(modifyUsernameReqDto.getUserId());
        if(foundUser.isEmpty()) {
            System.out.println("modifyUsername : 대상 없음");
            return new ApiRespDto<>("failed", "유저 정보를 다시 확인하세요.", null);
        }
        if(!foundUser.get().getUsername().equals(modifyUsernameReqDto.getOldUsername())) {
            System.out.println("modifyUsername : username 불일치");
            return new ApiRespDto<>("failed", "유저 정보를 다시 확인하세요.", null);
        }

        int result = userRepository.modifyUsername(modifyUsernameReqDto.toEntity());
        if (result != 1) {
            System.out.println("modifyUsername : SQL 변경 실패");
            return new ApiRespDto<>("failed", "update에 실패하였습니다. 나중에 다시 시도하세요.", null);
        }

        return new ApiRespDto<>("success", "username 변경에 성공하였습니다.", modifyUsernameReqDto);
    }

    public ApiRespDto<?> modifyPassword(ModifyPasswordReqDto modifyPasswordReqDto, Principal principal) {
        System.out.println("AccountService : modifyPassword");
        if(!modifyPasswordReqDto.getUserId().equals(principal.getUserId())) {
            System.out.println("modifyPassword : 인증 이상 - userId 불일치");
            return new ApiRespDto<>("failed", "잘못된 접근 입니다.", null);
        }

        Optional<User> foundUser = userRepository.findById(modifyPasswordReqDto.getUserId());
        if(foundUser.isEmpty()) {
            System.out.println("modifyPassword : 대상 없음");
            return new ApiRespDto<>("failed", "사용자 정보를 다시 확인하세요.", null);
        }
        if(!bCryptPasswordEncoder.matches(modifyPasswordReqDto.getOldPassword(),foundUser.get().getPassword())) {
            System.out.println("modifyPassword : password 틀림");
            return new ApiRespDto<>("failed", "사용자 정보를 다시 확인하세요.", null);
        }
        if(bCryptPasswordEncoder.matches(modifyPasswordReqDto.getNewPassword(),foundUser.get().getPassword())) {
            System.out.println("modifyPassword : 기존 password와 동일");
            return new ApiRespDto<>("failed", "기존 password와 동일합니다.", null);
        }

        int result = userRepository.modifyPassword(modifyPasswordReqDto.toEntity(bCryptPasswordEncoder));

        if (result != 1) {
            System.out.println("modifyPassword : SQL 변경 실패");
            return new ApiRespDto<>("failed", "update에 실패하였습니다. 나중에 다시 시도하세요.", null);
        }

        return new ApiRespDto<>("success", "password 변경에 성공하였습니다.", null);
    }

    public ApiRespDto<?> getUserList() {
        System.out.println("AccountService : getUserList");
        List<User> foundUserList = userRepository.findAll();
        if (foundUserList.isEmpty()) {
            System.out.println("getUserList : 리스트 없음");
            return new ApiRespDto<>("failed","userlist가 존재하지 않거나 조회에 실패하였습니다.", null);
        }

        System.out.println(foundUserList);
        return new ApiRespDto<>("success", "전체 조회에 성공하였습니다.",foundUserList);
    }

    public ApiRespDto<?> updateVerifyCode(Principal principal) {
        System.out.println("AccountService : updateVerifyCode");
        StringBuilder sb = new StringBuilder();
        Random rd = new Random();
        for (int i = 0; i < 5; i++) {
            sb.append(rd.nextInt(0,10));
        }
        Verify verify = Verify.builder()
                .userId(principal.getUserId())
                .verifyCode(sb.toString())
                .build();
        if (verifyRepository.findVerify(verify.getUserId()).isEmpty()) {
            int result = verifyRepository.addVerify(verify);
        }

        return new ApiRespDto<>("","", "");
    }

    public ApiRespDto<?> accountRegistry() {
        return new ApiRespDto<>("","","");
    }
}
