package com.yh.blogserver.controller.board;

import com.yh.blogserver.dto.BoardDto;
import com.yh.blogserver.dto.ResponseDto;
import com.yh.blogserver.dto.UserDto;
import com.yh.blogserver.entity.Board;
import com.yh.blogserver.service.board.BoardService;
import com.yh.blogserver.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/board")
public class BoardController {

    private final UserService userService;
    private final BoardService boardService;

    public BoardController(UserService userService, BoardService boardService) {
        this.userService = userService;
        this.boardService = boardService;
    }


    @PostMapping("/create")
    public ResponseEntity<?> createBoard(@RequestHeader(value = "Authorization") String token, @RequestBody BoardDto boardDto){
        ResponseDto<BoardDto> responseDto = new ResponseDto<>();

        String userId = userService.authenticatedUser(token);

        UserDto writer = userService.getUserByUserId(userId);
        boardDto.setUser(writer.toEntity());
        BoardDto createdBoard = boardService.createBoard(boardDto);

        responseDto.setItem(createdBoard);
        responseDto.setStatusCode(HttpStatus.CREATED.value());
        responseDto.setStatusMessage("Created");

        return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
    }

    @DeleteMapping("/{boardIndex}")
    public ResponseEntity<?> deleteBoard(@RequestHeader(value = "Authorization") String token, @PathVariable Long boardIndex){
        ResponseDto<String> responseDto = new ResponseDto<>();

        String userId = userService.authenticatedUser(token);
        String deleteMessage = "잘못된 접근입니다.";
        BoardDto boardDto = boardService.getBoard(boardIndex);

        // 플래그 활용 생각하기

        if(!boardDto.boardDeleteFlag && boardService.isWriterOf(boardIndex, userId)){
            deleteMessage = boardService.updateDeleteFlag(boardIndex);
        }

        responseDto.setItem(deleteMessage);
        responseDto.setStatusCode(HttpStatus.OK.value());
        responseDto.setStatusMessage("Deleted");

        return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
    }


}
