package com.korit.security_practice.service;

import com.korit.security_practice.dto.ApiRespDto;
import com.korit.security_practice.dto.SendMailReqDto;
import com.korit.security_practice.entity.User;
import com.korit.security_practice.entity.UserRole;
import com.korit.security_practice.entity.Verify;
import com.korit.security_practice.repository.UserRepository;
import com.korit.security_practice.repository.UserRoleRepository;
import com.korit.security_practice.repository.VerifyRepository;
import com.korit.security_practice.security.jwt.JwtUtils;
import com.korit.security_practice.security.model.Principal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MailService {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final JavaMailSender javaMailSender;
    private final UserRoleRepository userRoleRepository;
    private final VerifyRepository verifyRepository;


    public ApiRespDto<?> sendMail(SendMailReqDto sendMailReqDto, Principal principal) {
        System.out.println("MailService : sendMail");
        if(!sendMailReqDto.getEmail().equals(principal.getEmail())) {
            return new ApiRespDto<>("failed","잘못된 접근 입니다.",null);
        }
        Optional<User> foundUser = userRepository.findByEmail(sendMailReqDto.getEmail());
        if (foundUser.isEmpty()) {
            return new ApiRespDto<>("failed","사용자 정보가 일치하지 않습니다.",null);
        }

        User user = foundUser.get();
        boolean hasTempRole = user.getUserRoleList().stream()
                .anyMatch(userRole -> userRole.getRoleId() == 3);

        if (!hasTempRole) {
            return new ApiRespDto<>("failed","email 인증이 필요 없습니다.",null);
        }

        String verifyToken = jwtUtils.generateVerifyToken(user.getUserId().toString());

        SimpleMailMessage message = new SimpleMailMessage();

        Optional<Verify> verify = verifyRepository.findVerify(user.getUserId());
        if (verify.isEmpty()) {
            System.out.println("sendMail : 인증 코드 없음");
            return new ApiRespDto<>("failed","인증 코드가 없습니다. 재발행 해주세요.",null);
        }
        String code=verify.get().getVerifyCode();

        message.setTo(sendMailReqDto.getEmail());
        message.setSubject("[이메일 인증] 인증해주세요.");
        message.setText("숫자를 입력해 인증을 완료해주세요. : " + code);

        javaMailSender.send(message);

        return new ApiRespDto<>("success", "인증 메일이 전송되었습니다. 메일을 확인하세요", null);
    }

    public Map<String, Object> verify(String token) {
        Claims claims = null;
        Map<String, Object> resultMap = null;

        try {
            claims = jwtUtils.getClaims(token);
            String subject = claims.getSubject();

            if(!"VerifyToken".equals(subject)) {
                return resultMap = Map.of(
                        "status", "failed",
                        "message", "잘못된 접근 입니다."
                );
            }

            Integer userId = Integer.parseInt(claims.getId());
            Optional<User> foundUser = userRepository.findById(userId);
            if (foundUser.isEmpty()) {
                return resultMap = Map.of(
                        "status", "failed",
                        "message", "존재하지 않는 사용자 입니다."
                );
            }

            List<UserRole> userRoleList = foundUser.get().getUserRoleList();
            Optional<UserRole> tempUserRole = userRoleList.stream()
                    .filter(userRole -> userRole.getRoleId() == 3)
                    .findFirst();
            if(tempUserRole.isEmpty()) {
                return resultMap = Map.of(
                        "status", "failed",
                        "message", "인증이 필요없는 사용자 입니다."
                );
            }

            UserRole userRole = tempUserRole.get();
            userRole.setRoleId(2);
            int result = userRoleRepository.updateUserRole(userRole);
            System.out.println(result);
            return resultMap = Map.of(
                    "status", "success",
                    "message", "이메일 인증이 완료되었습니다.."
            );
        } catch (ExpiredJwtException e) {
            resultMap = Map.of(
                    "status", "failed",
                    "message", "만료된 인증 요청 입니다. \n 인증 메일을 다시 요청해주세요."
            );
        } catch (JwtException e) {
            resultMap = Map.of(
                    "status", "failed",
                    "message", "문제가 발생하였습니다. \n 인증 메일을 다시 요청해주세요."
            );
        }
        return resultMap;
    }
}
