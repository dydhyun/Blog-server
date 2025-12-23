package com.yh.blogserver.controller.mypage;

import com.yh.blogserver.config.JwtTokenProvider;
import com.yh.blogserver.dto.response.ResponseDto;
import com.yh.blogserver.dto.response.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "MyPage API", description = "마이페이지 사용자 계정 관련 조회,수정,탈퇴 API")
@RestController
@RequestMapping("/myPage")
public class MyPageController {

    @Operation(
            summary = "마이페이지 조회",
            description = """
                    로그인된 사용자가 마이페이지를 조회합니다.
                    Authorization 헤더에 JWT 토큰을 전달 해야합니다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "조회 실패 (토큰 만료 등)")
    })
    @GetMapping("")
    public ResponseEntity<ResponseDto<UserResponseDto>> getMyPage(){

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(null,"", HttpStatus.OK.value()));
    }

    @PatchMapping("")
    public ResponseEntity<ResponseDto<UserResponseDto>> patchMyPage(){

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(null,"", HttpStatus.OK.value()));
    }

    @PatchMapping("/delete")
    public ResponseEntity<ResponseDto<UserResponseDto>> deleteMyAccount(){

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(null,"", HttpStatus.OK.value()));
    }


}
