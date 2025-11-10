package com.yh.blogserver.controller.board;

import com.yh.blogserver.dto.request.BoardRequestDto;
import com.yh.blogserver.dto.response.BoardResponseDto;
import com.yh.blogserver.dto.response.ResponseDto;
import com.yh.blogserver.entity.User;
import com.yh.blogserver.service.board.BoardService;
import com.yh.blogserver.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @PostMapping("")
    public ResponseEntity<?> createBoard(@RequestHeader(value = "Authorization") String token,
                                         @RequestBody BoardRequestDto boardRequestDto){

        String userId = userService.authenticatedUser(token);
        log.info("[BOARD CREATE 요청] boardTitle={}, userId={}", boardRequestDto.boardTitle(), userId);

        User writer = userService.getUserEntityByUserId(userId);
        BoardRequestDto updatedDto = boardRequestDto.toBuilder().user(writer).build();

        BoardResponseDto createdBoard = boardService.createBoard(updatedDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdBoard);
    }

    @DeleteMapping("/{boardIndex}")
    public ResponseEntity<?> deleteBoard(@RequestHeader(value = "Authorization") String token,
                                         @PathVariable Long boardIndex){

        String userId = userService.authenticatedUser(token);
        log.info("[BOARD DELETE 요청] boardIndex={}, userId={}", boardIndex, userId);

        BoardResponseDto boardResponseDto = boardService.getBoard(boardIndex);

        if (!boardService.isWriterOf(boardIndex, userId)) {
            log.warn("[BOARD DELETE 실패] userId={} 가 게시글 {} 삭제 시도", userId, boardIndex);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("작성자만 삭제할 수 있습니다.");
        }

        if (boardResponseDto.boardDeleteFlag()) {
            log.warn("[BOARD DELETE 실패] 이미 삭제된 게시글입니다. boardIndex={}", boardIndex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 삭제된 게시글입니다.");
        }

        log.info("[BOARD DELETE 성공] userId={} 가 게시글 {} 삭제", userId, boardIndex);
        boardService.updateDeleteFlag(boardIndex);

        return ResponseEntity.status(HttpStatus.OK).body("삭제 되었습니다.");
    }


}
