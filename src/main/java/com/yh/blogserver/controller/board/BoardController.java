package com.yh.blogserver.controller.board;

import com.yh.blogserver.dto.request.BoardRequestDto;
import com.yh.blogserver.dto.response.BoardResponseDto;
import com.yh.blogserver.dto.response.ResponseDto;
import com.yh.blogserver.service.board.BoardService;
import com.yh.blogserver.service.user.UserService;
import com.yh.blogserver.util.message.ResponseMessage;
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
    public ResponseEntity<ResponseDto<BoardResponseDto>> createBoard(@RequestHeader(value = "Authorization") String token,
                                                   @RequestBody BoardRequestDto boardRequestDto){

        String userId = userService.authenticatedUser(token);
        log.info("[BOARD CREATE 요청] boardTitle={}, userId={}", boardRequestDto.boardTitle(), userId);

        BoardResponseDto createdBoard =
                boardService.createBoard(boardRequestDto, userId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseDto.success(createdBoard, HttpStatus.CREATED, ResponseMessage.CREATED.message()));
    }

    @PatchMapping("/{boardIndex}")
    public ResponseEntity<ResponseDto<Void>> deleteBoard(@RequestHeader(value = "Authorization") String token,
                                         @PathVariable Long boardIndex){

        String userId = userService.authenticatedUser(token);
        log.info("[BOARD DELETE 요청] boardIndex={}, userId={}", boardIndex, userId);

        boardService.deleteBoard(boardIndex, userId);

        log.info("[BOARD DELETE 성공] userId={} 가 boardIndex={} 삭제", userId, boardIndex);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(null, HttpStatus.OK, ResponseMessage.DELETED.message()));
    }


}
