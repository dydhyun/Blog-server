package com.yh.blogserver.util.message;

public enum UserMessage {
    LOGGED_IN("로그인 되었습니다."),
    LOGIN_FAIL("아이디 또는 비밀번호가 일치하지 않습니다."),
    USER_NOT_FOUND("존재하지 않는 사용자입니다."),
    JOIN_SUCCESS("회원가입이 완료되었습니다."),
    JOIN_FAIL("회원가입에 실패했습니다."),

    INVALID_FORMAT("올바르지 않은 형식입니다."),

    ID_MUST_NOT_BE_EMPTY("아이디를 입력하세요."),
    INVALID_USER_ID("사용할 수 없는 아이디 입니다."),
    AVAILABLE_USER_ID("사용 가능한 아이디 입니다."),

    NICKNAME_MUST_NOT_BE_EMPTY("닉네임을 입력하세요."),
    INVALID_USER_NICKNAME("사용할 수 없는 닉네임 입니다."),
    AVAILABLE_USER_NICKNAME("사용 가능한 닉네임 입니다."),

    PASSWORD_MUST_NOT_BE_EMPTY("비밀번호를 입력하세요."),
    INVALID_USER_PW("사용할 수 없는 비밀번호 입니다."),
    AVAILABLE_USER_PW("사용 가능한 비밀번호 입니다."),
    PASSWORD_LENGTH_MESSAGE("비밀번호는 8 ~ 16 글자 입니다."),;

    private final String message;

    UserMessage(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
