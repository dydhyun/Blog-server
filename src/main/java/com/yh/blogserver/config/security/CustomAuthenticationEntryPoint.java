package com.yh.blogserver.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yh.blogserver.config.security.message.AuthErrorMessage;
import com.yh.blogserver.config.security.response.SecurityErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
// JWT 관련 토큰없음, 인증실패 예외 분기
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException
    ) throws IOException, ServletException {
        AuthErrorMessage authErrorMessage = resolveError(authException);

        response.setStatus(authErrorMessage.status().value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(
                objectMapper.writeValueAsString(
                        SecurityErrorResponse.from(authErrorMessage)
                )
        );
    }


    private AuthErrorMessage resolveError(AuthenticationException exception) {

        Throwable cause = exception.getCause();

        if (cause instanceof ExpiredJwtException) {
            return AuthErrorMessage.EXPIRED_TOKEN;
        }

        if (cause instanceof JwtException) {
            return AuthErrorMessage.INVALID_TOKEN;
        }

        return AuthErrorMessage.TOKEN_REQUIRED;

    }
}
