package com.yh.blogserver.exception;

import com.yh.blogserver.util.message.MessageCode;

public class CustomException extends RuntimeException{

    private final MessageCode messageCode;

    public CustomException(MessageCode messageCode) {
        super(messageCode.message());
        this.messageCode = messageCode;
    }

    public MessageCode getMessageCode() {
        return messageCode;
    }

}
