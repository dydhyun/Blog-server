package com.yh.blogserver.controller.auth;

import com.yh.blogserver.dto.auth.TokenPair;
import com.yh.blogserver.dto.request.UserRequestDto;
import com.yh.blogserver.dto.response.ResponseDto;
import com.yh.blogserver.security.auth.CustomUserDetails;
import com.yh.blogserver.service.auth.AuthService;
import com.yh.blogserver.util.message.AuthMessage;
import com.yh.blogserver.util.message.UserMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Token API", description = "토큰 발급관련 API. 로그인, 로그아웃, refresh")
@RestController
@RequestMapping("/auth")
public class TokenController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    public TokenController(AuthService authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @Operation(
            summary = "로그인",
            description = """
                    아이디와 비밀번호를 입력하여 로그인합니다.
                    성공 시 JWT AccessToken 과 RefreshToken 을 발급합니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "로그인 실패")
    })
    @PostMapping("/login")
    public ResponseEntity<ResponseDto<Void>> login(@RequestBody UserRequestDto loginRequest){
        log.info("[USER LOGIN 요청] userRequestDto={}", loginRequest);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.userId(),loginRequest.userPw());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        // authenticationManager.authenticate(authenticationToken) 실행 시
        // CustomUserDetailsService 호출
        // DB 조회
        // passwordEncoder.matches() : componentScan 을 통해 PasswordEncodingConfig 자동주입
        // isEnabled() 호출
        // 실패 시 예외 throw
        // 성공 시 Authentication 반환

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String userId = customUserDetails.getUsername();

        TokenPair tokenPair = authService.issue(userId);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Bearer " + tokenPair.accessToken());
        // http 표준 규약 -> Authorization: <type> <credentials>

        httpHeaders.add("Set-Cookie",
                "refreshToken="+tokenPair.refreshToken()+
                        "; HttpOnly" +
                        "; Path=/" +
                        "; Max-Age=604800");

        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders)
                .body(ResponseDto.success(null, UserMessage.LOGGED_IN.message(), HttpStatus.OK.value()));
    }

    @Operation(
            summary = "로그아웃",
            description = "로그인 된 회원의 RefreshToken 을 제거하여 refresh 엔드포인트 접근을 제한합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            @ApiResponse(responseCode = "400", description = "로그아웃 실패"),
            @ApiResponse(responseCode = "404", description = "로그인 정보 없음")
    })
    @PostMapping("/logout")
    public ResponseEntity<ResponseDto<Void>> logout(@AuthenticationPrincipal CustomUserDetails customUserDetails){

        String userId = customUserDetails.getUsername();

        log.info("[USER LOGOUT 요청] userId={}", userId);

        authService.logout(userId);

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("Set-Cookie",
                "refreshToken="+
                        "; HttpOnly" +
                        "; Path=/" +
                        "; Max-Age=0");

        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders)
                .body(ResponseDto.success(null, AuthMessage.LOGGED_OUT.message(), HttpStatus.OK.value()));
    }

    @Operation(
            summary = "재발급",
            description = "AccessToken 이 만료되면 해당 API 를 통해 재발급 됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "재발급 성공"),
            @ApiResponse(responseCode = "400", description = "재발급 실패")
    })
    @PostMapping("/refresh")
    public ResponseEntity<ResponseDto<Void>> refresh(@CookieValue("refreshToken") String refreshToken) {

        TokenPair tokenPair = authService.reIssue(refreshToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + tokenPair.accessToken());
        headers.add("Set-Cookie",
                "refreshToken=" + tokenPair.refreshToken() +
                        "; HttpOnly; Path=/");

        return ResponseEntity.status(HttpStatus.OK).headers(headers)
                .body(ResponseDto.success(null, AuthMessage.REFRESH.message(), HttpStatus.OK.value()));
    }

}
