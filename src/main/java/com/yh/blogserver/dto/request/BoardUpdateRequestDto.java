package com.yh.blogserver.dto.request;

public record BoardUpdateRequestDto(
        String boardTitle,
        String boardContents,
        String thumbnailUrl
) {
}
