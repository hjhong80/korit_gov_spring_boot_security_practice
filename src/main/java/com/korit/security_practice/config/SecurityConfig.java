package com.korit.security_practice.config;

import com.korit.security_practice.security.filter.JwtAuthenticationFilter;
import com.korit.security_practice.security.handler.OAuth2SuccessHandler;
import com.korit.security_practice.service.OAuth2PrincipalService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor

public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuth2PrincipalService oAuth2PrincipalService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        System.out.println("SecurityConfig : bCryptPasswordEncoder");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource configurationSource() {
        System.out.println("SecurityConfig : configurationSource");
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOriginPattern(CorsConfiguration.ALL);
        corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);
        corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("SecurityConfig : securityFilterChain");
        http.cors(Customizer.withDefaults());
        http.csrf(csrf -> csrf.disable());
        http.formLogin(formLogin -> formLogin.disable());
        http.httpBasic(httpBasic -> httpBasic.disable());
        http.logout(logout -> logout.disable());
        http.sessionManagement(Session ->
                Session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/auth/**","/oauth2/**","/mail/verify","/login/**").permitAll();
            auth.anyRequest().authenticated();
        });

//        OAuth2 설정

        http.oauth2Login(oauth2 ->
                        oauth2.userInfoEndpoint(userInfo ->
                                        userInfo.userService(oAuth2PrincipalService))
                                .successHandler(oAuth2SuccessHandler)
        );

        return http.build();
    }



}
