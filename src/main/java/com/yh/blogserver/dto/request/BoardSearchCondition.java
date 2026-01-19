package com.yh.blogserver.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "게시글 검색조건으로 사용자가 프론트에서 요청하는 셀렉트 박스 값.")
public enum BoardSearchCondition {
    TITLE,
    CONTENT,
    WRITER
}
