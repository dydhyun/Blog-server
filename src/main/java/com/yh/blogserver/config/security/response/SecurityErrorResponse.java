package com.yh.blogserver.config.security.response;

import com.yh.blogserver.util.message.MessageCode;

public record SecurityErrorResponse(
        String code,
        String message
) {
    public static SecurityErrorResponse from(MessageCode messageCode){
        return new SecurityErrorResponse(
                messageCode.code(),
                messageCode.message()
        );
    }
}
