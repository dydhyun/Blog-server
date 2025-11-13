package com.yh.blogserver.controller.user;

import com.yh.blogserver.config.JwtTokenProvider;
import com.yh.blogserver.dto.response.ResponseDto;
import com.yh.blogserver.dto.request.UserRequestDto;
import com.yh.blogserver.dto.response.UserResponseDto;
import com.yh.blogserver.service.user.UserService;
import com.yh.blogserver.util.message.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    private static final String CHECK_MSG_KEY = "checkMessage";

    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Operation(summary = "아이디 중복체크", description = "회원가입 폼에서 유저아이디 문자열을 받아 중복 체크 하기 위한 엔드포인트")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용 가능한 아이디"),
            @ApiResponse(responseCode = "400", description = "사용할 수 없는 아이디")
    })
    @GetMapping("/exists/id")
    public ResponseEntity<ResponseDto<Map<String, String>>> userIdCheck(@RequestParam String userId){
        log.info("[userIdCheck 요청] userId={}", userId);

        Map<String, String> checkMsgMap = userService.userIdCheck(userId);

        return ResponseEntity
                .ok(ResponseDto.success(checkMsgMap, HttpStatus.OK, checkMsgMap.get(CHECK_MSG_KEY)));
    }

    @Operation(summary = "닉네임 중복체크", description = "회원가입 폼에서 유저닉네임 문자열을 받아 중복 체크 하기 위한 엔드포인트")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용 가능한 닉네임"),
            @ApiResponse(responseCode = "400", description = "사용할 수 없는 닉네임")
    })
    @GetMapping("/exists/nickname")
    public ResponseEntity<ResponseDto<Map<String, String>>> userNicknameCheck(@RequestParam String userNickname){
        log.info("[userNicknameCheck 요청] userNickname={}", userNickname);

        Map<String, String> checkMsgMap = userService.userNicknameCheck(userNickname);

        return ResponseEntity
                .ok(ResponseDto.success(checkMsgMap, HttpStatus.OK, checkMsgMap.get(CHECK_MSG_KEY)));
    }

    // 비밀번호는 검증이기에 postMapping
    @Operation(summary = "비밀번호 검증", description = "회원가입 폼에서 유저비밀번호 문자열을 받아 유효성 검사 하기 위한 엔드포인트")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용 가능한 비밀번호"),
            @ApiResponse(responseCode = "400", description = "사용할 수 없는 비밀번호")
    })
    @PostMapping("/verify-pw")
    public ResponseEntity<ResponseDto<Map<String, String>>> userPwCheck(@RequestParam String userPw){
        log.info("[userPwCheck 요청] userPw={}", userPw);

        Map<String, String> checkMsgMap = userService.userPwCheck(userPw);

        return ResponseEntity
                .ok(ResponseDto.success(checkMsgMap, HttpStatus.OK, checkMsgMap.get(CHECK_MSG_KEY)));
    }

    @Operation(summary = "유저 회원가입", description = "회원가입 폼에서 유저입력 정보를 받아 db 등록을 위한 엔드포인트")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "회원가입 실패")
    })
    @PostMapping("")
    public ResponseEntity<ResponseDto<UserResponseDto>> join(@RequestBody UserRequestDto userRequestDto){
        log.info("[USER JOIN 요청] userRequestDto={}", userRequestDto);

        UserResponseDto userResponseDto = userService.join(userRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseDto.success(userResponseDto, HttpStatus.CREATED, ResponseMessage.CREATED.message()));
    }

    @Operation(summary = "로그인", description = "아이디와 비밀번호를 userRequestDto 형태로 받아 로그인 하고 토큰 발급하기 위한 엔드포인트")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "로그인 실패")
    })
    @PostMapping("/login")
    public ResponseEntity<ResponseDto<UserResponseDto>> login(@RequestBody UserRequestDto loginRequest){
        log.info("[USER LOGIN 요청] userRequestDto={}", loginRequest);

        UserResponseDto loginedUserDto = userService.login(loginRequest);

        String token = jwtTokenProvider.createToken(loginedUserDto.userId(), loginedUserDto.isAdmin());
//        String refreshToken = jwtTokenProvider.createRefreshToken(loginedUserDto.getUserId());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Bearer " + token);
        // http 표준 규약 -> Authorization: <type> <credentials>

        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders)
                .body(ResponseDto.success(loginedUserDto, HttpStatus.OK, ResponseMessage.OK.message()));
    }

}