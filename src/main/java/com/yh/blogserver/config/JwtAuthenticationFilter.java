package com.yh.blogserver.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = parseJwt(request);

        if (token != null && jwtTokenProvider.validateToken(token)){
            String userId = jwtTokenProvider.getUserIdFromToken(token);

            boolean isAdmin = jwtTokenProvider.getUserGrantFromToken(token);
            // SecurityContext에 인증 객체 생성
            // authorities 는 Collection<? extends GrantedAuthority> 타입.
            // GrantedAuthority → 스프링 시큐리티에서 권한(roles, 권한 이름) 을 표현하는 인터페이스
            // 한 사용자에게 여러 권한이 있을 수 있기 때문에 리스트나 세트 형태로 제공
            List<GrantedAuthority> authorities = new ArrayList<>();
            // springSecurity 규약 -> ROLE prefix
            if (isAdmin) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            } else {
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            }

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userId, null, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String encodedToken = request.getHeader("Authorization");

        if (StringUtils.hasText(encodedToken) && encodedToken.startsWith("Bearer")){
            return encodedToken.substring(7);
        }

        return null;
    }

}
