package com.yh.blogserver.dto.request;

import com.yh.blogserver.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Schema(description = "게시글 생성,수정 등 사용자가 프론트에서 요청하는 DTO")
@Builder(toBuilder = true)
public record BoardRequestDto(
        @Schema(description = "게시글 인덱스. 사용자 입력 x", example = "1")
        Long boardIndex,
        @Schema(description = "게시글 작성자. 사용자 입력 x", example = "토큰으로 유저아이디 추출해서 유저 정보 세팅중.")
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
