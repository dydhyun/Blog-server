package com.yh.blogserver.controller.board;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yh.blogserver.dto.BoardDto;
import com.yh.blogserver.dto.ResponseDto;
import com.yh.blogserver.dto.UserDto;
import com.yh.blogserver.service.board.BoardService;
import com.yh.blogserver.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Base64;
import java.util.Map;

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

        // 메서드 따로 빼기 **
        Base64.Decoder decoder = Base64.getDecoder();
        String[] splitToken = token.split("\\.");
        String payloadJson = new String(decoder.decode(splitToken[1]));

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> payloadMap = null;
        try {
            payloadMap = mapper.readValue(payloadJson, Map.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
        //
        String userId = (String) payloadMap.get("userId");
        UserDto writer = userService.getUserByUserId(userId);
        boardDto.setUser(writer.toEntity());
        BoardDto createdBoard = boardService.createBoard(boardDto);

        responseDto.setItem(createdBoard);
        responseDto.setStatusCode(HttpStatus.CREATED.value());
        responseDto.setStatusMessage("Created");

        return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
    }

}
