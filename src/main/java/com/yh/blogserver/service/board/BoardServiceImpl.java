package com.yh.blogserver.service.board;

import com.yh.blogserver.dto.BoardDto;
import com.yh.blogserver.entity.Board;
import com.yh.blogserver.repository.board.BoardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;

    public BoardServiceImpl(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    public BoardDto createBoard(BoardDto boardDto) {

        Board board = boardDto.toEntity();
        
        BoardDto createdBoard = boardRepository.save(board).toDto();

        return createdBoard;
    }

    @Override
    public Boolean isWriterOf(BoardDto boardDto, String userId) {

        String writerId = boardDto.getUser().toDto().getUserId();

        return writerId.equals(userId);
    }

    @Override
    public String deleteBoard(BoardDto boardDto) {

        boardRepository.deleteByBoardIndex(boardDto.boardIndex);
        return "";
    }
}
