package com.yh.blogserver.util.message;

import org.springframework.http.HttpStatus;

public enum BoardMessage implements MessageCode {
    BOARD_CAN_NOT_EMPTY(HttpStatus.BAD_REQUEST, "400", "내용 혹은 제목을 입력하세요."),
    ALREADY_DELETED(HttpStatus.BAD_REQUEST,"400", "이미 삭제된 게시글 입니다."),
    FORBIDDEN_DELETE(HttpStatus.FORBIDDEN, "403", "작성자만 삭제할 수 있습니다."),
    WRONG_WRITER(HttpStatus.FORBIDDEN, "403","게시글 작성자가 아닙니다."),
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "게시글을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    BoardMessage(HttpStatus status, String code, String message) {
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
