package com.example.MutsaMarket.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenUtils {
    private final Key signingKey;

    private final JwtParser jwtParser;

    public JwtTokenUtils(@Value("${jwt.secret}") String jwtSecret) {
        this.signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.jwtParser = Jwts.parserBuilder().setSigningKey(this.signingKey).build();
    }

    //Token 생성
    public String generateToken(UserDetails userDetails) {
        Claims jwtClaims = Jwts.claims()
                .setSubject(userDetails.getUsername()) //토큰 주체
                .setIssuedAt(Date.from(Instant.now())) //토큰 발급 시간
                .setExpiration(Date.from(Instant.now().plusSeconds(3600))); //토큰 유효기간

        return Jwts.builder().setClaims(jwtClaims).signWith(signingKey).compact();
    }

    //Token 유효성 검증
    public boolean validate(String token) {
        try {
            jwtParser.parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.warn("invalid jwt: {}", e.getClass());
            return false;
        }
    }

    //사용자 정보 회수
    public Claims parseClaims(String token) {
        return jwtParser
                .parseClaimsJws(token)
                .getBody();
    }
}