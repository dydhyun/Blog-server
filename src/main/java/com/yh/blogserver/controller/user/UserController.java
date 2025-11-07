package com.yh.blogserver.controller.user;

import com.yh.blogserver.config.JwtTokenProvider;
import com.yh.blogserver.dto.ResponseDto;
import com.yh.blogserver.dto.UserDto;
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

    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/check/id")
    public ResponseEntity<?> userIdCheck(@RequestParam String userId){

        Map<String, String> checkMsgMap = userService.userIdCheck(userId);
        return ResponseEntity.status(HttpStatus.OK).body(checkMsgMap);
    }

    @GetMapping("/check/pw")
    public ResponseEntity<?> userPwCheck(@RequestParam String userPw){

        Map<String, String> checkMsgMap = userService.userPwCheck(userPw);
        return ResponseEntity.status(HttpStatus.OK).body(checkMsgMap);
    }

    @GetMapping("/check/nickname")
    public ResponseEntity<?> userNicknameCheck(@RequestParam String userNickname){

        Map<String, String> checkMsgMap = userService.userNicknameCheck(userNickname);
        return ResponseEntity.status(HttpStatus.OK).body(checkMsgMap);
    }

    @PostMapping("")
    public ResponseEntity<?> join(@RequestBody UserDto userDto){
        ResponseDto<UserDto> responseDto = new ResponseDto<>();

        UserDto joinedUserDto = userService.join(userDto);
        responseDto.setItem(joinedUserDto);
        responseDto.setStatusCode(HttpStatus.CREATED.value());
        responseDto.setStatusMessage("CREATED");

        return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto loginRequest){
        ResponseDto<UserDto> responseDto = new ResponseDto<>();

        UserDto loginedUserDto = userService.login(loginRequest);

        String token = jwtTokenProvider.createToken(loginedUserDto.getUserId(), loginedUserDto.getIsAdmin());
//        String refreshToken = jwtTokenProvider.createRefreshToken(loginedUserDto.getUserId());
        
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Bearer " + token);
        // http 표준 규약 -> Authorization: <type> <credentials>

        responseDto.setItem(loginedUserDto);
        responseDto.setStatusCode(HttpStatus.OK.value());
        responseDto.setStatusMessage("LOGIN SUCCESS");

        return ResponseEntity.status(responseDto.getStatusCode()).headers(httpHeaders).body(responseDto);
    }
    
    // 탈퇴기능 추가하기



}