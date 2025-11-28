package com.korit.security_practice.service;

import com.korit.security_practice.dto.ApiRespDto;
import com.korit.security_practice.dto.SigninReqDto;
import com.korit.security_practice.dto.SignupReqDto;
import com.korit.security_practice.entity.Role;
import com.korit.security_practice.entity.User;
import com.korit.security_practice.entity.UserRole;
import com.korit.security_practice.repository.UserRepository;
import com.korit.security_practice.repository.UserRoleRepository;
import com.korit.security_practice.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRoleRepository userRoleRepository;
    private final JwtUtils jwtUtils;

    public ApiRespDto<?> signup(SignupReqDto signupReqDto) {
        System.out.println("AuthService : signup");
        Optional<User> foundUserByEmail = userRepository.findByEmail(signupReqDto.getEmail());

        if (foundUserByEmail.isPresent()) {
            System.out.println("signin : 중복된 email 존재");
            return new ApiRespDto<>("failed", "사용할 수 없는 email 입니다.", null);
        }

        Optional<User> foundUserByUsername = userRepository.findByUsername(signupReqDto.getUsername());

        if (foundUserByUsername.isPresent()) {
            System.out.println("signin : 중복된 username 존재");
            return new ApiRespDto<>("failed", "사용할 수 없는 username 입니다.", null);
        }

        Optional<User> user = userRepository.addUser(signupReqDto.toEntity(bCryptPasswordEncoder));
        if (user.isEmpty()) {
            System.out.println("signin : SQL user_tb 추가 실패 ");
            return new ApiRespDto<>("failed", "가입에 실패하였습니다.", null);
        }

        UserRole userRole = UserRole.builder()
                .userId(user.get().getUserId())
                .roleId(3)
                .build();
        int result = userRoleRepository.addUserRole(userRole);

        if (result != 1) {
            System.out.println("signin : SQL user_role_tb 추가 실패 ");
            return new ApiRespDto<>("failed", "가입에 실패하였습니다.", null);
        }

        return new ApiRespDto<>("success", "가입을 환영합니다 "+ user.get().getUsername() + " 님", user.get());
    }


    public ApiRespDto<?> signin(SigninReqDto signinReqDto) {
        System.out.println("AuthService : signin");
        Optional<User> foundUser = userRepository.findByEmail(signinReqDto.getEmail());
        if(foundUser.isEmpty()) {
            System.out.println("signin : 존재하지 않는 email");
            return new ApiRespDto<>("failed","사용자 정보를 다시 확인해주세요.", null);
        }

        if(!bCryptPasswordEncoder.matches(signinReqDto.getPassword(),foundUser.get().getPassword())) {
            System.out.println("signin : password 틀림");
            return new ApiRespDto<>("failed","사용자 정보를 다시 확인해주세요.", null);
        }
        String token = jwtUtils.generateAccessToken(foundUser.get().getUserId().toString());
        return new ApiRespDto<>("success", "로그인 되었습니다.", token);
    }
}
