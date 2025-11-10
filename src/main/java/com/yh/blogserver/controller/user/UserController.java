package com.yh.blogserver.controller.user;

import com.yh.blogserver.config.JwtTokenProvider;
import com.yh.blogserver.dto.response.ResponseDto;
import com.yh.blogserver.dto.request.UserRequestDto;
import com.yh.blogserver.dto.response.UserResponseDto;
import com.yh.blogserver.service.user.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    private static final String CHECK_MSG_KEY = "checkMessage";

    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/exists/id")
    public ResponseEntity<ResponseDto<Map<String, String>>> userIdCheck(@RequestParam String userId){

        Map<String, String> checkMsgMap = userService.userIdCheck(userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(checkMsgMap, HttpStatus.OK, checkMsgMap.get(CHECK_MSG_KEY)));
    }

    @GetMapping("/exists/nickname")
    public ResponseEntity<ResponseDto<Map<String, String>>> userNicknameCheck(@RequestParam String userNickname){

        Map<String, String> checkMsgMap = userService.userNicknameCheck(userNickname);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(checkMsgMap, HttpStatus.OK, checkMsgMap.get(CHECK_MSG_KEY)));
    }

    // 비밀번호는 검증이기에 postMapping
    @PostMapping("/verify-pw")
    public ResponseEntity<ResponseDto<Map<String, String>>> userPwCheck(@RequestParam String userPw){

        Map<String, String> checkMsgMap = userService.userPwCheck(userPw);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(checkMsgMap, HttpStatus.OK, checkMsgMap.get(CHECK_MSG_KEY)));
    }

    @PostMapping("")
    public ResponseEntity<ResponseDto<UserResponseDto>> join(@RequestBody UserRequestDto userRequestDto){

        UserResponseDto userResponseDto = userService.join(userRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseDto.success(userResponseDto, HttpStatus.CREATED, "CREATED"));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<UserResponseDto>> login(@RequestBody UserRequestDto loginRequest){

        UserResponseDto loginedUserDto = userService.login(loginRequest);

        String token = jwtTokenProvider.createToken(loginedUserDto.userId(), loginedUserDto.isAdmin());
//        String refreshToken = jwtTokenProvider.createRefreshToken(loginedUserDto.getUserId());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Bearer " + token);
        // http 표준 규약 -> Authorization: <type> <credentials>

        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders)
                .body(ResponseDto.success(loginedUserDto, HttpStatus.OK, "LOGGED IN"));
    }
    
    // 탈퇴기능 추가하기


}