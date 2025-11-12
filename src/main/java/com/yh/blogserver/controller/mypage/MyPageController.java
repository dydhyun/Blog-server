package com.yh.blogserver.controller.mypage;

import com.yh.blogserver.config.JwtTokenProvider;
import com.yh.blogserver.dto.response.ResponseDto;
import com.yh.blogserver.dto.response.UserResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/myPage")
public class MyPageController {

    private final JwtTokenProvider jwtTokenProvider;

    public MyPageController(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("")
    public ResponseEntity<ResponseDto<UserResponseDto>> getMyPage(){

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(null,HttpStatus.OK,""));
    }

    @PatchMapping("")
    public ResponseEntity<ResponseDto<UserResponseDto>> patchMyPage(){

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(null,HttpStatus.OK,""));
    }

    @PatchMapping("/delete")
    public ResponseEntity<ResponseDto<UserResponseDto>> deleteMyAccount(){

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(null,HttpStatus.OK,""));
    }


}
