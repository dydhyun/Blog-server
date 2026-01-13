package com.yh.blogserver.dto.auth;

public record TokenPair(
        String accessToken,
        String refreshToken
) {
}
