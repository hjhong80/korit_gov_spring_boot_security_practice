package com.korit.security_practice.controller;

import com.korit.security_practice.dto.SigninReqDto;
import com.korit.security_practice.dto.SignupReqDto;
import com.korit.security_practice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupReqDto signupReqDto) {
        System.out.println("AuthController : signup");
        return ResponseEntity.ok(authService.signup(signupReqDto));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SigninReqDto signinReqDto) {
        System.out.println("AuthController : signin");
        return ResponseEntity.ok(authService.signin(signinReqDto));
    }

}
