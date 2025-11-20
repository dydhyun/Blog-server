package com.yh.blogserver.exception;

import com.yh.blogserver.dto.response.ResponseDto;
import com.yh.blogserver.util.message.MessageCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@RestControllerAdvice(annotations = {RestController.class},
//        basePackageClasses = {BlogController.class, BoardController.class, MyPageController.class, UserController.class})
//@RestControllerAdvice(basePackages = "com.yh.blogserver.controller")
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 200 : OK
    // 201 : CREATED
    // 202 : ACCEPTED : 요청접수됐는데 처리는 ㄴㄴ / 비동기
    // 204 : NO CONTENT : 처리성공, 반환없음

    // 400 : BAD_REQUEST : 잘못된 요청
    // 401 : UNAUTHORIZED : 접근권한 없음
    // 403 : FORBIDDEN : 리소스 접근 금지됨
    // 404 : NOT FOUND
    // 405 : METHOD NOT ALLOWED : 요청한 URI가 메서드 지원안함
    // 408 : REQUEST TIMEOUT
    // 409 : CONFLICT
    // 415 : UN SUPPORTED MEDIA TYPE

    // 500 : INTERNAL_SERVER_ERROR : 서버에러 발생 <- 최상위 에러 Exception에 매핑하기
    // 501 : NOT IMPLEMENTED
    // 502 : BAD GATEWAY
    // 504 : GATEWAY TIMEOUT

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseDto<?>> handleCustomException(CustomException e) {

        MessageCode messageCode = e.getMessageCode();

        return ResponseEntity.status(messageCode.status())
                .body(ResponseDto.error(messageCode.message(), messageCode.code()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<?>> handleGeneralException(Exception e) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseDto.error(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    // uri.startsWith("/swagger-ui") || uri.startsWith("/v3/api-docs") 요청 처리
    // swagger 발생가능 예외 :
    // HttpMessageNotReadableException
    // NullPointerException
    // MissingServletRequestParameterException

}
