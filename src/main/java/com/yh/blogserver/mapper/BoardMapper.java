package com.yh.blogserver.mapper;

import com.yh.blogserver.dto.request.BoardRequestDto;
import com.yh.blogserver.dto.response.BoardResponseDto;
import com.yh.blogserver.dto.response.UserResponseDto;
import com.yh.blogserver.entity.Board;
import com.yh.blogserver.entity.User;

public class BoardMapper {

    private BoardMapper(){
    }

    public static BoardResponseDto toBoardResponseDto(Board board){
        UserResponseDto userResponseDto = UserMapper.toUserResponseDto(board.getUser());

        return BoardResponseDto.builder()
                .userResponseDto(userResponseDto)
                .boardTitle(board.getBoardTitle())
                .boardContents(board.getBoardContents())
                .boardCreatedTime(board.getBoardCreatedTime())
                .boardViewCnt(board.getBoardViewCnt())
                .build();
    }

    public static Board fromDto(BoardRequestDto boardRequestDto, User user){
        return Board.builder()
                .boardIndex(boardRequestDto.boardIndex())
                .user(user)
                .boardTitle(boardRequestDto.boardTitle())
                .boardContents(boardRequestDto.boardContents())
                .boardCreatedTime(boardRequestDto.boardCreatedTime())
                .boardViewCnt(boardRequestDto.boardViewCnt())
                .build();
    }
}
