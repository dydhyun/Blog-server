package com.yh.blogserver.util.message;

import org.springframework.http.HttpStatus;

public enum AuthMessage implements MessageCode {
    LOGGED_IN(HttpStatus.OK, "auth-200", "로그인 되었습니다."),
    LOGGED_OUT(HttpStatus.OK, "auth-200", "로그아웃 되었습니다."),
    REFRESH(HttpStatus.OK, "auth-200", "재발급 되었습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"auth-404","로그아웃할 사용자 정보가 존재하지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    AuthMessage(HttpStatus status, String code, String message) {
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
