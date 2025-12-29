package com.yh.blogserver.config;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("[JWT FILTER] method={}, uri={}, Authorization={}",
                request.getMethod(),
                request.getRequestURI(),
                request.getHeader("Authorization"));

        String token = parseJwt(request);

        if (token == null) {
            //토큰이 없는 요청은 인증 대상이 아니므로
            //불필요한 JWT 검증 로직을 타지 않도록 early return
            filterChain.doFilter(request, response);
            return;
        }

        try {
            jwtTokenProvider.validateToken(token);
        } catch (JwtException e) {
            SecurityContextHolder.clearContext();
            // Spring Security는 ThreadLocal 기반이기 때문에
            // 인증 실패 시 SecurityContext를 명시적으로 정리하지 않으면
            // 이전 요청의 인증 정보가 남아 보안 이슈로 이어질 수 있음
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("""
            {"message": "Invalid JWT"}
            """);
            log.error("[JwtException] exception={}", e.getMessage());
            return;
            //AuthenticationEntryPoint 역할분리.

            //SpringMVC 를 타지않고,
            //다음과 같이 응답을 만들어 클라이언트에 즉시 응답함.
            //HTTP/1.1 401 Unauthorized
            //Content-Type: application/json
            //
            //{"message": "인증 토큰이 유효하지 않습니다"}
        }

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

        // 인증 성공 → SecurityContext 에 Authentication 세팅
        // Principal 객체로 사용자 ID 대신 CustomUserDetails 객체 사용 가능
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userId, null, authorities);

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("[JWT FILTER END] authentication={}", auth);


        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (!StringUtils.hasText(headerAuth)) {
            return null;
        }

        if (!headerAuth.startsWith("Bearer ")) {
            return null;
        }

        return headerAuth.substring(7).trim();

//        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")){
//            return headerAuth.substring(7);
//        }
//
//        return null;
    }

}
