package com.yh.blogserver.controller.admin;

import com.yh.blogserver.config.security.auth.CustomUserDetails;
import com.yh.blogserver.service.cleanup.BoardCleanupService;
import com.yh.blogserver.service.cleanup.UserCleanupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin CleanUp API", description = "보관기간이 만료 되었지만 자동 삭제가 안되었을 경우 수동으로 물리삭제 API")
@Slf4j
@RestController
@RequestMapping("/admin/cleanup")
public class AdminCleanupController {

    private final UserCleanupService userCleanupService;
    private final BoardCleanupService boardCleanupService;

    public AdminCleanupController(UserCleanupService userCleanupService, BoardCleanupService boardCleanupService) {
        this.userCleanupService = userCleanupService;
        this.boardCleanupService = boardCleanupService;
    }

    @Operation(summary = "스케줄링 배치 실패할 경우 직접 기한 만료 유저 삭제 API")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "400", description = "삭제 실패")
    })
    @DeleteMapping("/users")
    public ResponseEntity<Void> manualCleanUpExpiredUsers(
            @AuthenticationPrincipal CustomUserDetails customUserDetails){

        String adminId = customUserDetails.getUsername();
        log.info("[ADMIN CLEANUP User 수동 삭제 요청] adminId = {}", adminId);

        userCleanupService.deleteExpiredUsers();

        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "스케줄링 배치 실패할 경우 직접 기한 만료 게시글 삭제 API")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "400", description = "삭제 실패")
    })
    @DeleteMapping("/boards")
    public ResponseEntity<Void> manualCleanUpExpiredBoards(
            @AuthenticationPrincipal CustomUserDetails customUserDetails){

        String adminId = customUserDetails.getUsername();
        log.info("[ADMIN CLEANUP Board 수동 삭제 요청] adminId = {}", adminId);

        boardCleanupService.deleteExpiredBoards();

        return ResponseEntity.noContent().build();
    }

}
