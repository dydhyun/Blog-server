package com.yh.blogserver.controller.admin;

import com.yh.blogserver.dto.response.PageResponse;
import com.yh.blogserver.dto.response.ResponseDto;
import com.yh.blogserver.dto.response.UserResponseDto;
import com.yh.blogserver.security.auth.CustomUserDetails;
import com.yh.blogserver.service.admin.AdminUseCase;
import com.yh.blogserver.util.message.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin API", description = "관리자용 사용자, 게시글 관리 API")
@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminUseCase adminUseCase;

    public AdminController(AdminUseCase adminUseCase) {
        this.adminUseCase = adminUseCase;
    }

    @Operation(summary = "탈퇴 유저 조회 API",
            description = "deleteFlag 가 활성화된 유저 조회 API"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "조회 실패")
    })
    @GetMapping("/users/deleted")
    public ResponseEntity<ResponseDto<PageResponse<UserResponseDto>>> getDeletedUsers(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PageableDefault(size = 20, sort = "userCreatedTime") Pageable pageable){

        String adminId = customUserDetails.getUsername();
        log.info("[ADMIN getDeletedUsers 요청] adminId = {}", adminId);

        PageResponse<UserResponseDto> deletedUsers = adminUseCase.getDeletedUsers(pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(deletedUsers, ResponseMessage.OK.message(), HttpStatus.OK.value()));
    }

    @Operation(summary = "관리자 권한 유저 탈퇴 API",
            description = "관리자 권한으로 유저의 deleteFlag 활성화 (소프트딜리트) 하는 API"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저탈퇴 성공"),
            @ApiResponse(responseCode = "400", description = "유저탈퇴 실패")
    })
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ResponseDto<Void>> deleteUser(
            @AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable String userId){

        String adminId = customUserDetails.getUsername();
        log.info("[ADMIN deleteUser 요청] adminId = {}, targetId = {}",adminId, userId);

        adminUseCase.deleteUser(userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(null, ResponseMessage.OK.message(), HttpStatus.OK.value()));

    }

    @Operation(summary = "관리자 권한 유저 탈퇴 철회 API",
            description = "관리자 권한으로 유저의 deleteFlag 초기화 (계정 복구) 하는 API"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저복구 성공"),
            @ApiResponse(responseCode = "400", description = "유저복구 실패")
    })
    @PatchMapping("/users/{userId}")
    public ResponseEntity<ResponseDto<Void>> restoreUser(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,@PathVariable String userId){

        String adminId = customUserDetails.getUsername();
        log.info("[ADMIN restoreUser 요청] adminId = {}, targetId = {}", adminId, userId);

        adminUseCase.restoreUser(userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(null, ResponseMessage.OK.message(), HttpStatus.OK.value()));
    }

    @Operation(summary = "관리자 권한 게시글 삭제 API",
            description = "관리자 권한으로 게시글의 deleteFlag 활성화 (소프트딜리트) 하는 API"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "게시글 삭제 실패")
    })
    @DeleteMapping("/boards/{boardId}")
    public ResponseEntity<ResponseDto<Void>> deleteBoard(
            @AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long boardId){

        String adminId = customUserDetails.getUsername();
        log.info("[ADMIN deleteBoard 요청] adminId = {}, targetId = {}", adminId, boardId);

        adminUseCase.deleteBoard(boardId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(null, ResponseMessage.DELETED.message(), HttpStatus.OK.value()));
    }

    @Operation(summary = "관리자 권한 게시글 삭제 철회 API",
            description = "관리자 권한으로 게시글의 deleteFlag 초기화 (게시글 복구) 하는 API"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글복구 성공"),
            @ApiResponse(responseCode = "400", description = "게시글복구 실패")
    })
    @PatchMapping("/boards/{boardId}")
    public ResponseEntity<ResponseDto<Void>> restoreBoard(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,@PathVariable Long boardId){

        String adminId = customUserDetails.getUsername();
        log.info("[ADMIN restoreBoard 요청] adminId = {}, targetId = {}", adminId, boardId);

        adminUseCase.restoreBoard(boardId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(null, ResponseMessage.OK.message(), HttpStatus.OK.value()));
    }

}
