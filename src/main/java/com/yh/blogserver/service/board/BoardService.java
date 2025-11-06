package com.yh.blogserver.service.board;

import com.yh.blogserver.dto.BoardDto;

public interface BoardService {
    BoardDto createBoard(BoardDto boardDto);

    Boolean isWriterOf(BoardDto boardDto, String userId);

    String deleteBoard(BoardDto boardDto);
}
