package com.yh.blogserver.service.board;

import com.yh.blogserver.dto.request.BoardRequestDto;
import com.yh.blogserver.dto.request.BoardSearchCondition;
import com.yh.blogserver.dto.request.BoardUpdateRequestDto;
import com.yh.blogserver.dto.response.BlogBoardSummaryDto;
import com.yh.blogserver.dto.response.BoardResponseDto;
import com.yh.blogserver.dto.response.PageResponse;
import com.yh.blogserver.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface BoardService {
    BoardResponseDto createBoard(BoardRequestDto boardRequestDto, String userId);

    BoardResponseDto getBoard(Long boardIndex);

    BoardResponseDto updateBoard(Long boardIndex, BoardUpdateRequestDto boardRequestDto, String userId);

    void deleteBoard(Long boardIndex, String userId);

    Boolean isWriter(Board board, String userId);

    PageResponse<BoardResponseDto> searchBoards(BoardSearchCondition searchCondition, String keyword, Pageable pageable);

    List<BlogBoardSummaryDto> getNewestBoards(int limit);

    Page<BlogBoardSummaryDto> getBoardSummariesByUser(String userId, Pageable pageable);

    void deleteBoardByAdmin(Long boardId);
}
