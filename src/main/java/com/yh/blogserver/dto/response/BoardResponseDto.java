package com.yh.blogserver.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Schema(description = "서버에서 응답으로 돌려보내는 게시글관련 DTO")
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
