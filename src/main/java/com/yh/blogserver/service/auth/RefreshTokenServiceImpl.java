package com.yh.blogserver.service.auth;

import com.yh.blogserver.entity.RefreshToken;
import com.yh.blogserver.repository.auth.RefreshTokenRepository;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final long refreshTokenValidityMs;

    public RefreshTokenServiceImpl(
            RefreshTokenRepository refreshTokenRepository,
            @Value("${jwt.refreshTokenValidityTime}") long refreshTokenValidityMs) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenValidityMs = refreshTokenValidityMs;
    }

    @Override
    public void save(String userId, String refreshToken) {
        LocalDateTime expiresAt =
                LocalDateTime.now()
                        .plus(refreshTokenValidityMs, ChronoUnit.MILLIS);

        refreshTokenRepository.save(
                new RefreshToken(userId, refreshToken, expiresAt)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public void validate(String userId, String refreshToken) {
        RefreshToken token = refreshTokenRepository.findById(userId)
                .orElseThrow(() -> new JwtException("REFRESH_TOKEN_NOT_FOUND"));

        token.validate(refreshToken);
    }

    @Override
    public void delete(String userId) {
        refreshTokenRepository.deleteById(userId);
    }

}
