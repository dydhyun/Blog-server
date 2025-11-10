package com.yh.blogserver.exception;

import com.yh.blogserver.dto.response.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // 200 :
    // 201 :
    // 202 : 비동기
    // 204 :
    // 400 : BAD_REQUEST : 잘못된 요청
    // 401 :
    // 403 :
    // 404 :
    // 405 :
    // 408 :
    // 409 :
    // 415 :
    // 500 : INTERNAL_SERVER_ERROR : 서버에러 발생 <- 최상위 에러 Exception에 매핑하기
    // 502 :
    // 504 :

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDto<?>> handleIllegalArgument(IllegalArgumentException e) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseDto.error(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<?>> handleGeneralException(Exception e) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseDto.error(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

}
