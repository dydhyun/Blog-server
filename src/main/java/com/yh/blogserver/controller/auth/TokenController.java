package com.yh.blogserver.controller.auth;

import com.yh.blogserver.dto.auth.TokenPair;
import com.yh.blogserver.dto.request.UserRequestDto;
import com.yh.blogserver.dto.response.ResponseDto;
import com.yh.blogserver.dto.response.UserResponseDto;
import com.yh.blogserver.service.auth.AuthService;
import com.yh.blogserver.service.user.UserService;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Token API", description = "토큰 발급관련 API. 로그인, 로그아웃, refresh")
@RestController
@RequestMapping("/auth")
public class TokenController {

    private final AuthService authService;
    private final UserService userService;

    public TokenController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @Operation(
            summary = "로그인",
            description = """
                    아이디와 비밀번호를 입력하여 로그인합니다.
                    성공 시 Authorization 헤더에 JWT Access Token을 담아 반환합니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "로그인 실패")
    })
    @PostMapping("/login")
    public ResponseEntity<ResponseDto<UserResponseDto>> login(@RequestBody UserRequestDto loginRequest){
        log.info("[USER LOGIN 요청] userRequestDto={}", loginRequest);

        UserResponseDto loginedUserDto = userService.login(loginRequest);

        TokenPair tokenPair = authService.issue(loginedUserDto.userId());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Bearer " + tokenPair.accessToken());
        // http 표준 규약 -> Authorization: <type> <credentials>

        httpHeaders.add("Set-Cookie",
                "refreshToken="+tokenPair.refreshToken()+
                        "; HttpOnly" +
                        "; Path=/" +
                        "; Max-Age=604800");

        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders)
                .body(ResponseDto.success(loginedUserDto, UserMessage.LOGGED_IN.message(), HttpStatus.OK.value()));
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDto<Void>> logout(@AuthenticationPrincipal String userId){
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
