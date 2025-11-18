package com.yh.blogserver.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatusCode;

@Schema(description = "API 공통 응답 Dto")
public record ResponseDto<T> (
        @Schema(description = "응답 데이터")
        T data,

        @Schema(description = "응답 메시지")
        String message,

        @Schema(description = "응답 코드")
        int responseCode
) {
    public static <T> ResponseDto<T> success(T data, String message, int responseCode) {
        return new ResponseDto<>(data, message, responseCode);
    }

    public static <T> ResponseDto<T> error(String message, int responseCode) {
        return new ResponseDto<>(null, message, responseCode);
    }
}