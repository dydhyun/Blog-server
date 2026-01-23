package com.yh.blogserver.controller.mypage;

import com.yh.blogserver.dto.request.UserRequestDto;
import com.yh.blogserver.dto.request.UserUpdateRequestDto;
import com.yh.blogserver.dto.response.ResponseDto;
import com.yh.blogserver.dto.response.UserResponseDto;
import com.yh.blogserver.security.auth.CustomUserDetails;
import com.yh.blogserver.service.mypage.MyPageUseCase;
import com.yh.blogserver.util.message.ResponseMessage;
import com.yh.blogserver.util.message.UserMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "MyPage API", description = "마이페이지 사용자 계정 관련 조회,수정,탈퇴 API")
@RestController
@RequestMapping("/users/me")
public class MyPageController {

    private final MyPageUseCase myPageUseCase;

    public MyPageController(MyPageUseCase myPageUseCase) {
        this.myPageUseCase = myPageUseCase;
    }

    @Operation(
            summary = "마이페이지 조회",
            description = """
                    로그인된 사용자가 개인정보를 수정할 수 있는 마이페이지를 조회합니다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "조회 실패")
    })
    @GetMapping("")
    public ResponseEntity<ResponseDto<UserResponseDto>> getMyPage(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        String userId = customUserDetails.getUserId();

        UserResponseDto responseDto = myPageUseCase.getMyPage(userId);

        return ResponseEntity.ok(ResponseDto.success(
                responseDto, ResponseMessage.OK.message(), HttpStatus.OK.value()));
    }

    @Operation(
            summary = "마이페이지 사용자 정보 수정",
            description = "로그인 된 사용자가 원하는 필드에 사용자 입력을 받아 개인정보를 수정합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 정보 수정 성공"),
            @ApiResponse(responseCode = "400", description = "사용자 정보 수정 실패")
    })
    @PatchMapping("")
    public ResponseEntity<ResponseDto<Void>> updateMyPage(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                         @RequestBody UserUpdateRequestDto userUpdateRequestDto){
        String userId = customUserDetails.getUserId();

        myPageUseCase.updateMyPage(userId, userUpdateRequestDto);

        return ResponseEntity.ok(ResponseDto.success(
                null, UserMessage.USER_INFO_CHANGE.message(), UserMessage.USER_INFO_CHANGE.code()));
    }

    @DeleteMapping("")
    public ResponseEntity<ResponseDto<Void>> deleteMyAccount(){

        return ResponseEntity.ok(ResponseDto.success(
                null, UserMessage.USER_DELETE_SUCCESS.message(), UserMessage.USER_DELETE_SUCCESS.code()));
    }


}
