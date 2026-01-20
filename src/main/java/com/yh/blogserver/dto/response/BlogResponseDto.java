package com.yh.blogserver.dto.response;

import org.springframework.data.domain.Page;

public record BlogResponseDto (
        BlogHeaderDto blogHeader,
        BlogBoardSummaryDto blogBoardSummary,
        Page<BlogBoardSummaryDto> boardsSummaries
) {}
