package com.yh.blogserver.dto.request;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserRequestDto(
        Long userIndex,
        String userId,
        String userPw,
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
