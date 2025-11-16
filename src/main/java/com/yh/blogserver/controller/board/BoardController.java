package com.yh.blogserver.controller.board;

import com.yh.blogserver.dto.request.BoardRequestDto;
import com.yh.blogserver.dto.response.BoardResponseDto;
import com.yh.blogserver.dto.response.ResponseDto;
import com.yh.blogserver.service.board.BoardService;
import com.yh.blogserver.service.user.UserService;
import com.yh.blogserver.util.message.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Board API", description = "게시글 CRUD 관련 API")
@RestController
@RequestMapping("/boards")
public class BoardController {

    private static final Logger log = LoggerFactory.getLogger(BoardController.class);
    private final UserService userService;
    private final BoardService boardService;

    public BoardController(UserService userService, BoardService boardService) {
        this.userService = userService;
        this.boardService = boardService;
    }


    @Operation(
            summary = "게시글 작성",
            description = """
                    로그인된 사용자가 게시글을 작성합니다.
                    Authorization 헤더에 JWT 토큰을 전달해야 하며, BoardRequestDto 형식으로 본문을 전달합니다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "글 작성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (DTO 형식 오류 등)"),
            @ApiResponse(responseCode = "401", description = "인증 실패 (JWT 누락 또는 만료)")
    })
    @PostMapping("")
    public ResponseEntity<ResponseDto<BoardResponseDto>> createBoard(@RequestHeader(value = "Authorization") String token,
                                                                     @RequestBody BoardRequestDto boardRequestDto){

        String userId = userService.authenticatedUser(token);
        log.info("[BOARD CREATE 요청] boardTitle={}, userId={}", boardRequestDto.boardTitle(), userId);

        BoardResponseDto createdBoard =
                boardService.createBoard(boardRequestDto, userId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseDto.success(createdBoard, ResponseMessage.CREATED.message()));
    }


    @Operation(
            summary = "게시글 수정",
            description = """
                    게시글 작성자 본인만 게시글을 수정할 수 있습니다.
                    JWT가 필요하며, 작성자 본인이 아니라면 403 Forbidden이 반환됩니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "글 수정 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패 (JWT 관련)"),
            @ApiResponse(responseCode = "403", description = "본인 글이 아님"),
            @ApiResponse(responseCode = "404", description = "게시글 없음")
    })
    @PatchMapping("/{boardIndex}")
    public ResponseEntity<ResponseDto<BoardResponseDto>> updateBoard(@RequestHeader(value = "Authorization") String token,
                                                                     @PathVariable Long boardIndex,
                                                                     @RequestBody BoardRequestDto boardRequestDto){
        String userId = userService.authenticatedUser(token);
        log.info("[BOARD UPDATE 요청] boardIndex={}, userId={}", boardIndex, userId);

        BoardResponseDto updatedBoard = boardService.updateBoard(boardIndex, boardRequestDto, userId);

        log.info("[BOARD UPDATE 성공] userId={} 가 boardIndex={} 갱신", userId, boardIndex);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(updatedBoard, ResponseMessage.UPDATED.message()));
    }

    @Operation(
            summary = "게시글 삭제",
            description = """
                    게시글 작성자 본인만 게시글을 삭제할 수 있습니다.
                    JWT가 필요하며, 작성자 본인이 아니라면 403 Forbidden이 반환됩니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "글 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "이미 삭제된 게시글"),
            @ApiResponse(responseCode = "401", description = "인증 실패 (JWT 관련)"),
            @ApiResponse(responseCode = "403", description = "본인 글이 아님"),
            @ApiResponse(responseCode = "404", description = "게시글 없음")
    })
    @DeleteMapping("/{boardIndex}")
    public ResponseEntity<ResponseDto<Void>> deleteBoard(@RequestHeader(value = "Authorization") String token,
                                                         @PathVariable Long boardIndex){

        String userId = userService.authenticatedUser(token);
        log.info("[BOARD DELETE 요청] boardIndex={}, userId={}", boardIndex, userId);

        boardService.deleteBoard(boardIndex, userId);

        log.info("[BOARD DELETE 성공] userId={} 가 boardIndex={} 삭제", userId, boardIndex);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(null, ResponseMessage.DELETED.message()));
    }// 204 NO_CONTENT 는 body가 없음. -> ResponseEntity.noContent().build()


}
