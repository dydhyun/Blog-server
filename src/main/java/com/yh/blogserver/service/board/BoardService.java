package com.yh.blogserver.service.board;

import com.yh.blogserver.dto.request.BoardRequestDto;
import com.yh.blogserver.dto.response.BoardResponseDto;

public interface BoardService {
    BoardResponseDto createBoard(BoardRequestDto boardRequestDto);

    Boolean isWriterOf(Long boardIndex, String userId);

    String updateDeleteFlag(Long boardIndex);

    BoardResponseDto getBoard(Long boardIndex);
}
