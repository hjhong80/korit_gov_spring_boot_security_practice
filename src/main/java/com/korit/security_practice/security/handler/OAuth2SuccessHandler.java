package com.korit.security_practice.security.handler;

import com.korit.security_practice.entity.OAuth2User;
import com.korit.security_practice.entity.User;
import com.korit.security_practice.repository.OAuth2UserRepository;
import com.korit.security_practice.repository.UserRepository;
import com.korit.security_practice.security.jwt.JwtUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final OAuth2UserRepository oAuth2UserRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("OAuth2SuccessHandler : onAuthenticationSuccess");
        DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        String provider = defaultOAuth2User.getAttribute("provider");
        String providerUserId = defaultOAuth2User.getAttribute("providerUserId");
        String email = defaultOAuth2User.getAttribute("email");

        System.out.println("---------------------------------------------------");
        System.out.println(provider);
        System.out.println(providerUserId);
        System.out.println(email);
        System.out.println("---------------------------------------------------");

        Optional<OAuth2User> foundOAuth2USer = oAuth2UserRepository.getOAuth2UserByProviderAndProviderUserID(provider,providerUserId);
        if (foundOAuth2USer.isEmpty()) {
            System.out.println("http://localhost:3000/auth/oauth2?provider=" + provider + "&providerUserId=" + providerUserId + "&email=" + email);
            response.sendRedirect("http://localhost:3000/auth/oauth2?provider=" + provider + "&providerUserId=" + providerUserId + "&email=" + email);
            return;
        }

        Optional<User> foundUser = userRepository.findById(foundOAuth2USer.get().getUserId());
        String accessToken = null;
        if (foundUser.isPresent()) {
            accessToken = jwtUtils.generateAccessToken(Integer.toString(foundUser.get().getUserId()));

        }

        response.sendRedirect("http://localhost:3000/auth/oauth2/signin?accessToken=" + accessToken);

    }
}
