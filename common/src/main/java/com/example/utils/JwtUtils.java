package com.example.utils;

import com.example.jwt.JwtProperties;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.SecretKey;

import io.jsonwebtoken.security.Keys;

import java.time.Instant;
import java.util.Date;

@Slf4j
@Component
@ConditionalOnBean(JwtProperties.class)
public class JwtUtils {

    @Autowired
    private JwtProperties jwtProperties;

    private String vaultTransitKey;

    private SecretKey key;

    @PostConstruct
    public void init(){
        if (vaultTransitKey != null){
            this.setKey(vaultTransitKey);
        }
    }

    /**
     * jwt token을 생성하기 위한 key object 생성
     * valt가 초기화 할 때 해당 함수를 호출하여 키 사이즈는 64byte가 되어야 하므로 자리수가 부족한 경우 ' '값을 붙임.
     *
     * @param secret
     */
    public void setKey(String secret) {
        if (StringUtils.isBlank(secret))
            throw new IllegalArgumentException("The secret key for JWT is requried!!");

        key = Keys.hmacShaKeyFor(StringUtils.rightPad(secret, 64, " ").getBytes());
    }

    /**
     * JWT 토큰 생성: 사용자 정보를 포한한 Access Token 생성.
     *
     * @param claims
     * @return
     */
    public String generateAccesToken(Claims claims){
        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date())
                .expiration((this.calculateExpireDate(this.jwtProperties.getExpireTimeAccessToken())))
                .signWith(getKey(), Jwts.SIG.HS512)
                .compact();
    }

    /**
     * JWT Refresh 토큰 생성: Acces Tokken이 만료되었을 때 재발급 여부를 확인하기 위한 Refresh Token 생성.
     * @return
     */
    public String generateRefreshToken(){
        return Jwts.builder()
                .issuedAt(new Date())
                .expiration(calculateExpireDate(jwtProperties.getExpireTimeRefreshToken()))
                .signWith(getKey(), Jwts.SIG.HS512)
                .compact();
    }

    /**
     * token에서 subject 조회
     *
     * @param token
     * @return
     */
    public String getSubjectFromJwtToken(String token){
        return Jwts.parser()
                .verifyWith(getKey()).build()
                .parseSignedClaims(token).getPayload().getSubject();
    }

    /**
     * token에서 Claims(metadata) 정보 조회
     *
     * @param token
     * @return
     */
    public Claims getClaims(String token){
        return Jwts.parser()
                .verifyWith(getKey()).build()
                .parseSignedClaims(token).getPayload();
    }

    /**
     * JWT token 검사
     *
     * @param authToken
     * @return
     */
    public boolean validateJwtToken(String authToken){
        try {
            Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token : {}, {}", authToken, e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired : {}, {}", authToken, e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported : {}, {}", authToken, e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty : {}, {}", authToken, e.getMessage());
        }

        return false;
    }

    /**
     * 만료 날짜 계산
     *
     * @param expireTime
     * @return
     */
    private Date calculateExpireDate(long expireTime){
        return new Date(Date.from(Instant.now()).getTime() + expireTime);
    }

    /**
     * JWT 토큰 생성에 필요한 Key 리턴
     *
     * @return
     */
    private SecretKey getKey(){ return key; }
}
