package com.korit.security_practice.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OAuth2PrincipalService extends DefaultOAuth2UserService {
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("OAuth2PrincipalService : loadUser");
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String,Object> attributes = oAuth2User.getAttributes();

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String email = null;
        String providerUserId = null;

        switch(provider) {
            case "google" :
                providerUserId = attributes.get("sub").toString();
                email = (String) attributes.get("email");
                break;
            case "naver" :
//                Map<String, Object> response = (Map<String, Object>) attributes.get("response");
//                providerUserId = response.get("id").toString();
//                email = (String) response.get("email");
//                break;
            case "kakao" :
//                providerUserId = attributes.get("id").toString();
//                Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
//                email = (String) kakaoAccount.get("email");
//                break;
            default :
                break;
        }

        Map<String,Object> newAttributes = Map.of(
                "providerUserId", providerUserId,
                "provider", provider,
                "email", email
        );
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_TEMPORARY"));

        return new DefaultOAuth2User(authorities, newAttributes,"providerUserId");
    }
}
