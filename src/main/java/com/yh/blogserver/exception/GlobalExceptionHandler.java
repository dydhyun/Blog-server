package com.yh.blogserver.exception;

import com.yh.blogserver.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // 400 : BAD_REQUEST : 잘못된 요청
    // 500 : INTERNAL_SERVER_ERROR : 서버에러 발생 <- 최상위 에러 Exception에 매핑하기

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDto<?>> handleIllegalArgument(IllegalArgumentException e) {
        ResponseDto<Object> responseDto = new ResponseDto<>();
        responseDto.setStatusCode(HttpStatus.BAD_REQUEST.value());
        responseDto.setStatusMessage("BAD_REQUEST");
        responseDto.setErrorMessage(e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<?>> handleGeneralException(Exception e) {
        ResponseDto<Object> responseDto = new ResponseDto<>();
        responseDto.setStatusCode(HttpStatus.BAD_REQUEST.value());
        responseDto.setStatusMessage("BAD_REQUEST");
        responseDto.setErrorMessage(e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }

}
