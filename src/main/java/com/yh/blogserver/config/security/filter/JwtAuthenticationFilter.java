package com.yh.blogserver.config.security.filter;

import com.yh.blogserver.config.security.jwt.JwtTokenProvider;
import com.yh.blogserver.security.auth.CustomUserDetailsService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, AuthenticationEntryPoint authenticationEntryPoint, CustomUserDetailsService customUserDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.customUserDetailsService = customUserDetailsService;
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
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
            return;
        }

        try {
            jwtTokenProvider.validateToken(token);

            String userId = jwtTokenProvider.getUserIdFromToken(token);

            UserDetails userDetails =
                    customUserDetailsService.loadUserByUsername(userId);

            if (!userDetails.isEnabled()) {
                throw new DisabledException("Disabled user");
            }

            // SecurityContext에 인증 객체 생성
            // 인증 성공 → SecurityContext 에 Authentication 세팅
            // Principal 객체로 사용자 ID 대신 CustomUserDetails 객체 사용 가능
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);


            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            log.info("[JWT FILTER END] authentication={}", auth);

        }
        catch ( DisabledException e) {
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(request, response, e);
            return;
        }

        catch (JwtException e) {
            SecurityContextHolder.clearContext();
            // Spring Security는 ThreadLocal 기반이기 때문에
            // 인증 실패 시 SecurityContext를 명시적으로 정리하지 않으면
            // 이전 요청의 인증 정보가 남아 보안 이슈로 이어질 수 있음
            authenticationEntryPoint.commence(
                    request,
                    response,
                    new InsufficientAuthenticationException("JWT authentication failed", e)
            );
            return;
        }

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
