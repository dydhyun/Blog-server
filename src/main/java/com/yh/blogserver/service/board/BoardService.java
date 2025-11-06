package com.yh.blogserver.service.board;

import com.yh.blogserver.dto.BoardDto;

public interface BoardService {
    BoardDto createBoard(BoardDto boardDto);

    Boolean isWriterOf(Long boardIndex, String userId);

    String updateDeleteFlag(Long boardIndex);

    BoardDto getBoard(Long boardIndex);
}
