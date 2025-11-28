package com.korit.security_practice.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    private final Key KEY;

    public JwtUtils(@Value("${jwt.secret}") String secret) {
        KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }


    public String generateAccessToken(String id) {
        System.out.println("JwtUtils : generateAccessToken");
        return Jwts.builder()
                .subject("AccessToken")
                .id(id)
                .expiration(new Date(new Date().getTime()+(1000L * 60L * 60L * 24L * 30L)))
                .signWith(KEY)
                .compact();
    }

    public Claims getClaims(String token) throws JwtException {
        System.out.println("JwtUtils : getClaims");
        JwtParserBuilder jwtParserBuilder = Jwts.parser();

        jwtParserBuilder.setSigningKey(KEY);
        JwtParser jwtParser = jwtParserBuilder.build();
        return jwtParser.parseClaimsJws(token).getBody();       // 순수 Claims JWT를 파싱
    }

    public boolean isBearer(String token) {
        System.out.println("JwtUtils : isBearer");
        if(token == null) {
            return false;
        }
        if (!token.startsWith("Bearer ")) {
            return false;
        }
        return true;
    }

    public String removeBearer(String bearerToken) {
        System.out.println("JwtUtils : removeBearer");
        return bearerToken.substring(7).trim();
//        return bearerToken.replaceFirst("Bearer ", "");
    }



}
