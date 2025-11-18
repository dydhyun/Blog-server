package com.yh.blogserver.util.message;

import org.springframework.http.HttpStatus;

public interface MessageCode {
    HttpStatus status();
    String code();
    String message();
}
