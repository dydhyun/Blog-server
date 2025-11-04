package com.yh.blogserver.service.board;

import com.yh.blogserver.dto.BoardDto;
import com.yh.blogserver.entity.Board;
import com.yh.blogserver.repository.board.BoardRepository;
import org.springframework.stereotype.Service;

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
}
