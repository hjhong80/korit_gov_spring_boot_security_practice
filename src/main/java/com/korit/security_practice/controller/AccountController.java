package com.korit.security_practice.controller;

import com.korit.security_practice.dto.ModifyPasswordReqDto;
import com.korit.security_practice.dto.ModifyUsernameReqDto;
import com.korit.security_practice.dto.VerifyReqDto;
import com.korit.security_practice.security.model.Principal;
import com.korit.security_practice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/modify/username")
    public ResponseEntity<?> modifyUsername(@RequestBody ModifyUsernameReqDto modifyUsernameReqDto, @AuthenticationPrincipal Principal principal) {
        System.out.println("AccountController : modifyUsername");
        return ResponseEntity.ok(accountService.modifyUsername(modifyUsernameReqDto,principal));
    }

    @PostMapping("/modify/password")
    public ResponseEntity<?> modifyPassword(@RequestBody ModifyPasswordReqDto modifyPasswordReqDto, @AuthenticationPrincipal Principal principal) {
        System.out.println("AccountController : modifyPassword");
        return ResponseEntity.ok(accountService.modifyPassword(modifyPasswordReqDto,principal));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getUserList() {
        System.out.println("AccountController : getUserList");
        return ResponseEntity.ok(accountService.getUserList());
    }

    @GetMapping("/updatecode")
    public ResponseEntity<?> updateVerifyCode(@AuthenticationPrincipal Principal principal) {
        System.out.println("AccountController : updateVerifyCode");
        return ResponseEntity.ok(accountService.updateVerifyCode(principal));
    }

    @PostMapping("/registry")
    public ResponseEntity<?> accountRegistry(@RequestBody VerifyReqDto verifyReqDto,@AuthenticationPrincipal Principal principal) {
        System.out.println("AccountController : accountRegistry");
        return ResponseEntity.ok(accountService.accountRegistry(verifyReqDto, principal));
    }


}
