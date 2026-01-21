package com.yh.blogserver.dto.response;

import java.time.LocalDateTime;

public record BlogBoardSummaryDto(
        Long boardIndex,
        String userId,
        String boardTitle,
        String thumbnailUrl,
        LocalDateTime boardCreatedTime,
        long boardViewCnt
) {}
