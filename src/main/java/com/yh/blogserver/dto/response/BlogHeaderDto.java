package com.yh.blogserver.dto.response;

public record BlogHeaderDto(
        String userId,
        String nickname,
        String profileImageUrl,
        String description,
        long boardCount
) {}
