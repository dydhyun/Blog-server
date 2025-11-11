package com.yh.blogserver.service.board;

import com.yh.blogserver.dto.request.BoardRequestDto;
import com.yh.blogserver.dto.response.BoardResponseDto;
import com.yh.blogserver.entity.User;

public interface BoardService {
    BoardResponseDto createBoard(BoardRequestDto boardRequestDto, String userId);

    BoardResponseDto getBoard(Long boardIndex);

    void deleteBoard(Long boardIndex, String userId);

}
