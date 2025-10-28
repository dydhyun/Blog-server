package com.yh.blogserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseDto<T> {
    private T item;
    private List<T> itemList;
    private int statusCode;
    private String statusMessage;
    private String errorMessage;
}
