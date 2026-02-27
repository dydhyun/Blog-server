package com.yh.blogserver.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ApiTimeFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain) throws IOException, ServletException {

        long startTime = System.currentTimeMillis();

        filterChain.doFilter(servletRequest, servletResponse);

        long endTime = System.currentTimeMillis();

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        System.out.println(
                "[API TIME] " +
                        request.getMethod() + " " +
                        request.getRequestURI() +
                        " → " + (endTime - startTime) + " ms"
        );

    }
}
