package com.yh.blogserver.service.board;

import com.yh.blogserver.dto.request.BoardRequestDto;
import com.yh.blogserver.dto.request.BoardSearchCondition;
import com.yh.blogserver.dto.response.BoardResponseDto;
import com.yh.blogserver.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface BoardService {
    BoardResponseDto createBoard(BoardRequestDto boardRequestDto, String userId);

    BoardResponseDto getBoard(Long boardIndex);

    BoardResponseDto updateBoard(Long boardIndex, BoardRequestDto boardRequestDto, String userId);

    void deleteBoard(Long boardIndex, String userId);

    Boolean isWriter(Board board, String userId);

    Page<BoardResponseDto> searchBoards(BoardSearchCondition searchCondition, String keyword, Pageable pageable);
}
