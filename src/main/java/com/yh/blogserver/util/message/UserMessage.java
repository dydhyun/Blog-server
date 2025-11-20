package com.yh.blogserver.util.message;

import org.springframework.http.HttpStatus;

public enum UserMessage implements MessageCode{
    AUTHENTICATED_USER_FAIL(HttpStatus.NOT_FOUND, "user-404", "payload 에서 userId를 추출하지 못했습니다."),
    LOGGED_IN(HttpStatus.OK, "user-200", "로그인 되었습니다."),
    LOGIN_FAIL(HttpStatus.BAD_REQUEST, "user-400", "아이디 또는 비밀번호가 일치하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "user-400", "존재하지 않는 사용자입니다."),
    JOIN_SUCCESS(HttpStatus.CREATED, "user-201", "회원가입이 완료되었습니다."),

    JOIN_FAIL(HttpStatus.FORBIDDEN, "user-403", "회원가입에 실패했습니다."),

    INVALID_FORMAT(HttpStatus.BAD_REQUEST, "user-400", "올바르지 않은 형식입니다."),

    ID_MUST_NOT_BE_EMPTY(HttpStatus.BAD_REQUEST, "user-400", "아이디를 입력하세요."),
    ID_CAN_NOT_UNDER_8(HttpStatus.BAD_REQUEST, "user-400", "아이디는 8자 이상 가능합니다."),
    INVALID_USER_ID(HttpStatus.BAD_REQUEST, "user-400", "사용할 수 없는 아이디 입니다."),
    AVAILABLE_USER_ID(HttpStatus.OK, "user-200", "사용 가능한 아이디 입니다."),

    CAN_NOT_INCLUDE_SPACE(HttpStatus.BAD_REQUEST, "user-400", "공백을 포함할 수 없습니다."),

    NICKNAME_MUST_NOT_BE_EMPTY(HttpStatus.BAD_REQUEST, "user-400", "닉네임을 입력하세요."),
    INVALID_USER_NICKNAME(HttpStatus.BAD_REQUEST, "user-400", "사용할 수 없는 닉네임 입니다."),
    AVAILABLE_USER_NICKNAME(HttpStatus.OK, "user-200", "사용 가능한 닉네임 입니다."),

    PASSWORD_MUST_NOT_BE_EMPTY(HttpStatus.BAD_REQUEST, "user-400", "비밀번호를 입력하세요."),
    INVALID_USER_PW(HttpStatus.BAD_REQUEST, "user-400", "사용할 수 없는 비밀번호 입니다."),
    AVAILABLE_USER_PW(HttpStatus.OK, "user-200", "사용 가능한 비밀번호 입니다."),
    PASSWORD_LENGTH_MESSAGE(HttpStatus.BAD_REQUEST, "user-400", "비밀번호는 8 ~ 16 글자 입니다."),
    PASSWORD_NOT_VALID_MESSAGE(HttpStatus.BAD_REQUEST, "user-400", "비밀번호에는 하나 이상의 특수문자가 포함되어야 합니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    UserMessage(HttpStatus status, String code, String message) {
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
