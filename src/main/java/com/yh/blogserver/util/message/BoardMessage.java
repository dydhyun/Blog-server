package com.yh.blogserver.util.message;

import org.springframework.http.HttpStatus;

public enum BoardMessage implements MessageCode {
    BOARD_CAN_NOT_EMPTY(HttpStatus.BAD_REQUEST, "board-400", "내용 혹은 제목을 입력하세요."),
    ALREADY_DELETED(HttpStatus.BAD_REQUEST,"board-400", "이미 삭제된 게시글 입니다."),
    FORBIDDEN_DELETE(HttpStatus.FORBIDDEN, "board-403", "작성자만 삭제할 수 있습니다."),
    WRONG_WRITER(HttpStatus.FORBIDDEN, "board-403","게시글 작성자가 아닙니다."),
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "board-404", "게시글을 찾을 수 없습니다."),
    SEARCH_KEYWORD_EMPTY(HttpStatus.BAD_REQUEST, "board-400", "검색어를 입력 하세요.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    BoardMessage(HttpStatus status, String code, String message) {
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
