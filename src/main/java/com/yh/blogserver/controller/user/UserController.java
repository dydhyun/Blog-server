package com.yh.blogserver.controller.user;

import com.yh.blogserver.dto.ResponseDto;
import com.yh.blogserver.dto.UserDto;
import com.yh.blogserver.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/userId-check")
    public ResponseEntity<?> userIdCheck(@RequestBody UserDto userDto){
        ResponseDto<Map<String,String>> responseDto = new ResponseDto<>();

        Map<String, String> checkMsgMap = userService.userIdCheck(userDto.getUserId());
        responseDto.setItem(checkMsgMap);
        responseDto.setStatusCode(HttpStatus.OK.value());
        responseDto.setStatusMessage("OK");

        return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
    }

    @PostMapping("/userPw-check")
    public ResponseEntity<?> userPwCheck(@RequestBody UserDto userDto){
        ResponseDto<Map<String,String>> responseDto = new ResponseDto<>();

        Map<String, String> checkMsgMap = userService.userPwCheck(userDto.getUserPw());
        responseDto.setItem(checkMsgMap);
        responseDto.setStatusCode(HttpStatus.OK.value());
        responseDto.setStatusMessage("OK");

        return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
    }

    @PostMapping("/userNickname-check")
    public ResponseEntity<?> userNicknameCheck(@RequestBody UserDto userDto){
        ResponseDto<Map<String,String>> responseDto = new ResponseDto<>();

        Map<String, String> checkMsgMap = userService.userNicknameCheck(userDto.getNickname());
        responseDto.setItem(checkMsgMap);
        responseDto.setStatusCode(HttpStatus.OK.value());
        responseDto.setStatusMessage("OK");

        return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
    }

    @PostMapping("/join")
    public ResponseEntity<?> tempJoin(@RequestBody UserDto userDto){
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

        responseDto.setItem(loginedUserDto);
        responseDto.setStatusCode(HttpStatus.OK.value());
        responseDto.setStatusMessage("LOGIN SUCCESS");

        return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
    }



}