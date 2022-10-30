package com.szs.jobis.Config.Provider;

import com.szs.jobis.Repository.tokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;
import java.util.stream.Collectors;

// 리프레시 토큰 생성, 검증
// TokenProvider 기능을 확장
// 토큰 생성 시 가중치를 클레임에 넣는다.
// 토큰 검증 시 유저 가중치 > 리프레시 토큰 가중치라면 리프레시 토큰은 유효하지 않다.

public class RefreshTokenProvider extends TokenProvider {

    public RefreshTokenProvider(String secret, long tokenValidityInSeconds) {
        super(secret, tokenValidityInSeconds);
    }

    // 토큰 생성
    public String createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = System.currentTimeMillis();
        Date IssuedAtDate = new Date(now);
        Date ExpirationDate = new Date(now + this.tokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setIssuedAt(IssuedAtDate)
                .setExpiration(ExpirationDate)
                .compact();
    }
}