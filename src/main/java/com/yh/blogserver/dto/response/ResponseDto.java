package com.yh.blogserver.dto.response;


import org.springframework.http.HttpStatus;

public record ResponseDto<T> (
    T data,
    HttpStatus status,
    String message
) {
    public static <T> ResponseDto<T> success(T data, HttpStatus status, String message) {
        return new ResponseDto<>(data, status, message);
    }

    public static <T> ResponseDto<T> error(HttpStatus status, String message) {
        return new ResponseDto<>(null, status, message);
    }
}