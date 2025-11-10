package com.yh.blogserver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDto<T> {
    private T item;
    private int statusCode;
    private String message;
}
