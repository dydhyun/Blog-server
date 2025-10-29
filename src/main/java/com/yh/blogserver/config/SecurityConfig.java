package com.yh.blogserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // React 프론트가 요청 보내는 REST API 모두 허용
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/admin/**").hasAuthority("ADMIN")
//                        /api/admin/** 접근 권한은 서비스 레이어에서 isAdmin 검사로 처리
                        .anyRequest().permitAll()
                )
                // CORS 허용 (React 통신 용도 추가하기)
                .cors(cors -> {})
                // CSRF 비활성화 (REST API : 세션 없이 토큰으로 인증)
                .csrf(csrf -> csrf.disable())
                // formLogin, httpBasic 둘 다 사용하지 않음
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .build();
    }
}
