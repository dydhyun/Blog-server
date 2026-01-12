package com.yh.blogserver.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yh.blogserver.config.security.message.AuthErrorMessage;
import com.yh.blogserver.config.security.response.SecurityErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
// 권한부족 예외 분기
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {
        AuthErrorMessage authErrorMessage = AuthErrorMessage.ACCESS_DENIED;

        response.setStatus(authErrorMessage.status().value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(
                objectMapper.writeValueAsString(
                        SecurityErrorResponse.from(authErrorMessage)
                )
        );
    }
}
