package com.yh.blogserver.controller.user;

import com.yh.blogserver.dto.ResponseDto;
import com.yh.blogserver.dto.UserDto;
import com.yh.blogserver.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("api/userId-check")
    public ResponseEntity<?> userIdCheck(@RequestBody UserDto userDto){
        ResponseDto<Map<String,String>> responseDto = new ResponseDto<>();

//        try {
            Map<String, String> checkMsgMap = userService.userIdCheck(userDto.getUserId());
            responseDto.setItem(checkMsgMap);
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("OK");
//        } catch (Exception e) {
//            responseDto.setStatusCode(HttpStatus.BAD_REQUEST.value());
//            responseDto.setStatusMessage("BAD_REQUEST");
//            responseDto.setErrorMessage(e.getMessage());
//        }
        return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
    }



    @PostMapping("api/join")
    public ResponseEntity<?> tempJoin(@RequestBody UserDto userDto){
        ResponseDto<UserDto> responseDto = new ResponseDto<>();

//        try {
            UserDto joinedUserDto = userService.join(userDto);
            responseDto.setItem(joinedUserDto);
            responseDto.setStatusCode(HttpStatus.CREATED.value());
            responseDto.setStatusMessage("CREATED");
//        }
//        catch (Exception e){
//            responseDto.setStatusCode(HttpStatus.BAD_REQUEST.value());
//            responseDto.setStatusMessage("BAD_REQUEST");
//            responseDto.setErrorMessage("");
//        }
        return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
    }


}
