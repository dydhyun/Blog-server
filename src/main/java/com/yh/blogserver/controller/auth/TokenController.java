package com.yh.blogserver.controller.auth;

import com.yh.blogserver.dto.auth.TokenPair;
import com.yh.blogserver.service.auth.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Token API", description = "refreshToken 만료시 토큰 재발급 API")
@RestController
@RequestMapping("/auth")
public class TokenController {

    private final AuthService authService;

    public TokenController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(@CookieValue("refreshToken") String refreshToken) {

        TokenPair tokenPair = authService.refresh(refreshToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + tokenPair.accessToken());
        headers.add("Set-Cookie",
                "refreshToken=" + tokenPair.refreshToken() +
                        "; HttpOnly; Path=/");

        return ResponseEntity.ok().headers(headers).build();
    }

}
