package com.yh.blogserver.config.security.message;

import com.yh.blogserver.util.message.MessageCode;
import org.springframework.http.HttpStatus;

public enum AuthErrorMessage implements MessageCode {
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "auth-401", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "auth-401", "만료된 토큰입니다."),
    TOKEN_REQUIRED(HttpStatus.UNAUTHORIZED, "auth-401", "인증 토큰이 필요합니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "auth-403", "접근 권한이 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    AuthErrorMessage(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus status(){
        return status;
    }

    public String code(){
        return code;
    }

    public String message() {
        return message;
    }

}
