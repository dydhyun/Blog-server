package com.yh.blogserver.controller.user;

import com.yh.blogserver.config.JwtTokenProvider;
import com.yh.blogserver.dto.response.ResponseDto;
import com.yh.blogserver.dto.request.UserRequestDto;
import com.yh.blogserver.dto.response.UserResponseDto;
import com.yh.blogserver.service.user.UserService;
import com.yh.blogserver.util.message.ResponseMessage;
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

    @GetMapping("/exists/id")
    public ResponseEntity<ResponseDto<Map<String, String>>> userIdCheck(@RequestParam String userId){
        log.info("[userIdCheck 요청] userId={}", userId);

        Map<String, String> checkMsgMap = userService.userIdCheck(userId);

        return ResponseEntity
                .ok(ResponseDto.success(checkMsgMap, HttpStatus.OK, checkMsgMap.get(CHECK_MSG_KEY)));
    }

    @GetMapping("/exists/nickname")
    public ResponseEntity<ResponseDto<Map<String, String>>> userNicknameCheck(@RequestParam String userNickname){
        log.info("[userNicknameCheck 요청] userNickname={}", userNickname);

        Map<String, String> checkMsgMap = userService.userNicknameCheck(userNickname);

        return ResponseEntity
                .ok(ResponseDto.success(checkMsgMap, HttpStatus.OK, checkMsgMap.get(CHECK_MSG_KEY)));
    }

    // 비밀번호는 검증이기에 postMapping
    @PostMapping("/verify-pw")
    public ResponseEntity<ResponseDto<Map<String, String>>> userPwCheck(@RequestParam String userPw){
        log.info("[userPwCheck 요청] userPw={}", userPw);

        Map<String, String> checkMsgMap = userService.userPwCheck(userPw);

        return ResponseEntity
                .ok(ResponseDto.success(checkMsgMap, HttpStatus.OK, checkMsgMap.get(CHECK_MSG_KEY)));
    }

    @PostMapping("")
    public ResponseEntity<ResponseDto<UserResponseDto>> join(@RequestBody UserRequestDto userRequestDto){
        log.info("[USER JOIN 요청] userRequestDto={}", userRequestDto);

        UserResponseDto userResponseDto = userService.join(userRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseDto.success(userResponseDto, HttpStatus.CREATED, ResponseMessage.CREATED.message()));
    }

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
    
    // 탈퇴기능 추가하기


}