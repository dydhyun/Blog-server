package com.yh.blogserver.util.message;

public enum BoardMessage {
    ALREADY_DELETED("이미 삭제된 게시글 입니다."),
    FORBIDDEN_DELETE("작성자만 삭제할 수 있습니다."),
    BOARD_NOT_FOUND("게시글을 찾을 수 없습니다.");

    private final String message;

    BoardMessage(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
