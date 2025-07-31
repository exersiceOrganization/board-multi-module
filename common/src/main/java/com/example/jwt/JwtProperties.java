package com.example.jwt;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter @Setter @ToString
@Configuration
@ConditionalOnProperty("jwt.expire-time-access-token")
@ConfigurationProperties(prefix = JwtProperties.JWT_PREFIX)
public class JwtProperties {
    public static final String JWT_PREFIX = "jwt";
    private long expireTimeAccessToken;
    private long expireTimeRefreshToken;
}
