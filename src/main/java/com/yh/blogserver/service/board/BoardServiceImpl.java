package com.yh.blogserver.service.board;

import com.yh.blogserver.dto.request.BoardRequestDto;
import com.yh.blogserver.dto.response.BoardResponseDto;
import com.yh.blogserver.entity.Board;
import com.yh.blogserver.entity.User;
import com.yh.blogserver.mapper.BoardMapper;
import com.yh.blogserver.repository.board.BoardRepository;
import com.yh.blogserver.service.user.UserService;
import org.springframework.stereotype.Service;


@Service
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;
    private final UserService userService;

    public BoardServiceImpl(BoardRepository boardRepository, UserService userService) {
        this.boardRepository = boardRepository;
        this.userService = userService;
    }

    @Override
    public BoardResponseDto createBoard(BoardRequestDto boardRequestDto) {

        User user = userService.getUserEntityByUserId(boardRequestDto.user().getUserId());
        Board board = BoardMapper.fromDto(boardRequestDto, user);
        
        return BoardMapper.toBoardResponseDto(boardRepository.save(board));
    }

    @Override
    public Boolean isWriterOf(Long boardIndex, String userId) {
        return boardRepository.existsByBoardIndexAndUser_UserId(boardIndex, userId);
    }

// jpa 변경감지
// jpa 영속성영역에 올리기위해 조회
// @Transactional
    @Override
    public String updateDeleteFlag(Long boardIndex) {
        Board board = boardRepository.findById(boardIndex)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        board.markAsDeleted();

        BoardResponseDto deletedBoard = BoardMapper.toBoardResponseDto(boardRepository.saveAndFlush(board));
        // 스프링 배치 + 스케쥴러 + deleteFlag 이용해서 게시글 지우기
        // deleteBoard
//        boardRepository.deleteByBoardIndex(boardDto.boardIndex);
        return "board deleted";
    }

    @Override
    public BoardResponseDto getBoard(Long boardIndex) {
        return BoardMapper.toBoardResponseDto(boardRepository.findById(boardIndex)
                .orElseThrow(()-> new IllegalArgumentException("게시글을 찾을 수 없습니다.")));
    }
}
