package com.yh.blogserver.config.security.message;

import com.yh.blogserver.util.message.MessageCode;
import org.springframework.http.HttpStatus;

public enum AuthErrorMessage implements MessageCode {
    BAD_CREDENTIALS(HttpStatus.BAD_REQUEST, "auth-400_1", "잘못된 아이디 또는 비밀번호 입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "auth-401_1", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "auth-401_2", "만료된 토큰입니다."),
    TOKEN_REQUIRED(HttpStatus.UNAUTHORIZED, "auth-401_3", "인증 토큰이 필요합니다."),
    DISABLED_USER(HttpStatus.UNAUTHORIZED, "auth-401_4", "탈퇴한 유저입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "auth-403_1", "접근 권한이 없습니다."),
    NOT_EXIST_USER(HttpStatus.NOT_FOUND, "auth-404_1", "존재하지 않는 유저입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    AuthErrorMessage(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    @Override
    public HttpStatus status(){
        return status;
    }

    @Override
    public String code(){
        return code;
    }

    @Override
    public String message() {
        return message;
    }

}
