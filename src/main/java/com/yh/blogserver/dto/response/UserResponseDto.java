package com.yh.blogserver.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserResponseDto(
        Long userIndex,
        String userId,
        String username,
        String nickname,
        String address,
        String addressDetail,
        String pNumber,
        String email,
        Boolean isAdmin,
        LocalDateTime createdDate
) {
}
