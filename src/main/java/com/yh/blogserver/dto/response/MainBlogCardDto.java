package com.yh.blogserver.dto.response;

public record MainBlogCardDto(
        BlogHeaderDto blogHeaderDto,
        BlogBoardSummaryDto blogBoardSummaryDto
) {
}
