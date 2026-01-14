package com.yh.blogserver.config.security;

import com.yh.blogserver.config.security.filter.JwtAuthenticationFilter;
import com.yh.blogserver.config.security.handler.CustomAccessDeniedHandler;
import com.yh.blogserver.config.security.handler.CustomJwtAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           CustomJwtAuthenticationEntryPoint authenticationEntryPoint,
                                           CustomAccessDeniedHandler accessDeniedHandler,
                                           JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        return http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/auth/refresh").permitAll()
                                .requestMatchers("/auth/logout").authenticated()
                                .requestMatchers(HttpMethod.GET, "/boards/**").permitAll()
                                .requestMatchers("/boards/**").authenticated()
                                .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
//                        /api/admin/** 접근 권한은 서비스 레이어에서 isAdmin 검사로 처리/ 아니고 filter 에서 토큰값확인
                                .anyRequest().permitAll()
                )
                // CORS 허용 (React 통신 용도 추가하기)
                .cors(cors -> {})
                // CSRF 비활성화 (REST API : 세션 없이 토큰으로 인증)
                .csrf(csrf -> csrf.disable())
                // security 패키지의 커스텀예외 분기 처리해주기
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                // 두 번째 매개변수(기본 form 로그인 클래스) 앞에 첫 번째 매개변수(filter)를 실행하는 설정
                // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 보다 먼저 실행시켜서,
                // JWT 만으로도 SecurityContext 에 사용자 인증 정보를 세팅하도록 만드는 설정
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // formLogin, httpBasic 둘 다 사용하지 않음
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .build();
    }
//    @Bean
//    public SecurityFilterChain swaggerFilterChain(HttpSecurity http) throws Exception {
//        http
//                .securityMatcher("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
//                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
//                .csrf(csrf -> csrf.disable())
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//        return http.build();
//    }

}
