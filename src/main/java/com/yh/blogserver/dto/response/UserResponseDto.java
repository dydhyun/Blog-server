package com.yh.blogserver.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Schema(description = "서버에서 응답으로 돌려보내는 회원관련 DTO")
@Builder
public record UserResponseDto(
//        Long userIndex,
        String userId,
        String username,
        String nickname,
        String address,
        String addressDetail,
        String pNumber,
        String email,
        boolean isAdmin,
        LocalDateTime userCreatedTime,
        String description,
        String profileImageUrl
) {
}
