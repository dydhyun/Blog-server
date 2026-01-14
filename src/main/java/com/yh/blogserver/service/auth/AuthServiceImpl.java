package com.yh.blogserver.service.auth;

import com.yh.blogserver.config.security.jwt.JwtTokenProvider;
import com.yh.blogserver.dto.auth.TokenPair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService{

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    public AuthServiceImpl(JwtTokenProvider jwtTokenProvider, RefreshTokenService refreshTokenService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public TokenPair issue(String userId) {

        String accessToken = jwtTokenProvider.createToken(userId);
        String refreshToken = jwtTokenProvider.createRefreshToken(userId);

        refreshTokenService.save(userId, refreshToken);
        return new TokenPair(accessToken, refreshToken);
    }

    @Override
    public TokenPair reIssue(String refreshToken) {

        jwtTokenProvider.validateToken(refreshToken);
        String userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
//        boolean isAdmin = jwtTokenProvider.getUserGrantFromToken(refreshToken);

        refreshTokenService.validate(userId, refreshToken);
        refreshTokenService.delete(userId);

        return issue(userId);
    }

    @Override
    @Transactional
    public void logout(String userId) {

        refreshTokenService.delete(userId);
    }


}
