package com.yh.blogserver.dto.request;

import com.yh.blogserver.entity.User;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BoardRequestDto(
        Long boardIndex,
        User user,
        String boardTitle,
        String boardContents,
        LocalDateTime boardCreatedTime,
        Long boardViewCnt,
        boolean boardDeleteFlag
) {
}
