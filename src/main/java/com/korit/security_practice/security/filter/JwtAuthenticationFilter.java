package com.korit.security_practice.security.filter;

import com.korit.security_practice.entity.User;
import com.korit.security_practice.repository.UserRepository;
import com.korit.security_practice.security.jwt.JwtUtils;
import com.korit.security_practice.security.model.Principal;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements Filter {
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("JwtAuthenticationFilter : doFilter");
        HttpServletRequest req = (HttpServletRequest) servletRequest;

        List<String> methodList = List.of("POST", "PUT", "GET", "PATCH", "DELETE");
        if (!methodList.contains(req.getMethod())) {
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }

        String authorization = req.getHeader("Authorization");
        System.out.println("Bearer Token : " + authorization);

        if (jwtUtils.isBearer(authorization)) {
            String accessToken = jwtUtils.removeBearer(authorization);
            try {
                Claims claims = jwtUtils.getClaims(accessToken);
                String id = claims.getId();
                Integer userId = Integer.parseInt(id);
                Optional<User> foundUser = userRepository.findById(userId);
                foundUser.ifPresentOrElse((user -> {
                    Principal principal = Principal.builder()
                            .userId(user.getUserId())
                            .username(user.getUsername())
                            .password(user.getPassword())
                            .email(user.getEmail())
                            .userRoleList(user.getUserRoleList())
                            .build();
                    Authentication authentication = new UsernamePasswordAuthenticationToken(principal,"",principal.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);       // 인증 완료
                }), () -> {
                    throw new AuthenticationServiceException("인증 실패");
                });

            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

}
