package com.yh.blogserver.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BoardResponseDto(
        Long boardIndex,
        UserResponseDto userResponseDto,
        String boardTitle,
        String boardContents,
        LocalDateTime boardCreatedTime,
        Long boardViewCnt,
        boolean boardDeleteFlag
) {
}
