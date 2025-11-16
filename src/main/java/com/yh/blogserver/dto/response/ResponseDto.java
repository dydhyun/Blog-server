package com.yh.blogserver.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

@Schema(description = "API 공통 응답 Dto")
public record ResponseDto<T> (
        @Schema(description = "응답 데이터")
        T data,

        @Schema(description = "HTTP Status")
        HttpStatus status,

        @Schema(description = "응답 메시지")
        String message
) {
    public static <T> ResponseDto<T> success(T data, HttpStatus status, String message) {
        return new ResponseDto<>(data, status, message);
    }

    public static <T> ResponseDto<T> error(HttpStatus status, String message) {
        return new ResponseDto<>(null, status, message);
    }
}