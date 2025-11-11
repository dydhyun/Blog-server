package com.yh.blogserver.util.message;

public enum ResponseMessage {
    CREATED("CREATED"),
    UPDATED("UPDATED"),
    DELETED("DELETED"),
    OK("OK"),
    BAD_REQUEST("BAD REQUEST"),
    FORBIDDEN("FORBIDDEN");

    private final String message;

    ResponseMessage(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
