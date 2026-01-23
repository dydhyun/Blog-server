package com.yh.blogserver.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자가 마이페이지 사용자 정보 수정에 요청하는 DTO")
public record UserUpdateRequestDto(
        String userPw,
        String nickname,
        String address,
        String addressDetail,
        String pNumber,
        String email,
        String description,
        String profileImageUrl
) {}
