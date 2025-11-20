package com.yh.blogserver.dto.request;

import com.yh.blogserver.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record BoardRequestDto(
        @Schema(description = "게시글 인덱스. 사용자 입력 x", example = "1")
        Long boardIndex,
        @Schema(description = "게시글 작성자. 사용자 입력 x", example = "유저인덱스를 담고있음.")
        User user,
        @Schema(description = "게시글 제목. 빈 제목 불가", example = "게시글 1")
        String boardTitle,
        @Schema(description = "게시글 내용. 빈 내용 불가", example = "게시글 1 내용 입니다.")
        String boardContents,
        @Schema(description = "게시글 생성시간. 사용자 입력 x", example = "2025-11-20 17:42:26.517182")
        LocalDateTime boardCreatedTime,
        @Schema(description = "게시글 조회수. 사용자 입력 x", example = "1")
        Long boardViewCnt
) {
}
