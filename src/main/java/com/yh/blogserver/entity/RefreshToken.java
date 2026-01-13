package com.yh.blogserver.entity;

import io.jsonwebtoken.JwtException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(nullable = false, length = 500)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    protected RefreshToken() {}

    public RefreshToken(String userId, String token, LocalDateTime expiresAt) {
        this.userId = userId;
        this.token = token;
        this.expiresAt = expiresAt;
    }

    public void validate(String refreshToken) {
        if (!this.token.equals(refreshToken)) {
            throw new JwtException("REFRESH_TOKEN_MISMATCH");
        }
        if (this.expiresAt.isBefore(LocalDateTime.now())) {
            throw new JwtException("REFRESH_TOKEN_EXPIRED");
        }
    }
}
