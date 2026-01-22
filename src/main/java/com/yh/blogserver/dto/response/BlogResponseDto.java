package com.yh.blogserver.dto.response;


public record BlogResponseDto (
        BlogHeaderDto blogHeader,
        PageResponse<BlogBoardSummaryDto> boardsSummaries
) {}
