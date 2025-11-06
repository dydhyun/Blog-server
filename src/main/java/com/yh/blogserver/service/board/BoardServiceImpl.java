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
    public Boolean isWriterOf(Long boardIndex, String userId) {
        BoardDto boardDto = boardRepository.findById(boardIndex).get().toDto();

        String writerId = boardDto.getUser().toDto().getUserId();

        return writerId.equals(userId);
    }

    @Override
    public String updateDeleteFlag(Long boardIndex) {

        Board board = boardRepository.findById(boardIndex)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        board.markAsDeleted();

        BoardDto deletedBoard = boardRepository.saveAndFlush(board).toDto();
        // 스프링 배치 + 스케쥴러 + deleteFlag 이용해서 게시글 지우기
        // deleteBoard
//        boardRepository.deleteByBoardIndex(boardDto.boardIndex);
        return "board deleted";
    }

    @Override
    public BoardDto getBoard(Long boardIndex) {
        return boardRepository.findById(boardIndex)
                .orElseThrow(()-> new IllegalArgumentException("게시글을 찾을 수 없습니다.")).toDto();
    }
}
