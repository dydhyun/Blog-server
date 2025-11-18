package com.yh.blogserver.service.board;

import com.yh.blogserver.dto.request.BoardRequestDto;
import com.yh.blogserver.dto.response.BoardResponseDto;
import com.yh.blogserver.entity.Board;
import com.yh.blogserver.entity.User;
import com.yh.blogserver.exception.CustomException;
import com.yh.blogserver.mapper.BoardMapper;
import com.yh.blogserver.repository.board.BoardRepository;
import com.yh.blogserver.service.user.UserService;
import com.yh.blogserver.util.message.BoardMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class BoardServiceImpl implements BoardService {

    private static final Logger log = LoggerFactory.getLogger(BoardServiceImpl.class);
    private final BoardRepository boardRepository;
    private final UserService userService;

    public BoardServiceImpl(BoardRepository boardRepository, UserService userService) {
        this.boardRepository = boardRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto boardRequestDto, String userId) {

        if (boardRequestDto.boardTitle().trim().isEmpty() || boardRequestDto.boardContents().trim().isEmpty()){
            log.warn("[BOARD CREATE 실패] userId={} 게시글제목 혹은 내용이 비어있습니다.", userId);
            throw new CustomException(BoardMessage.BOARD_CAN_NOT_EMPTY);
        }

        User user = userService.getUserEntityByUserId(userId);
        Board board = BoardMapper.fromDto(boardRequestDto, user);
        Board savedBoard = boardRepository.save(board);

        return BoardMapper.toBoardResponseDto(savedBoard);
    }

    @Override
    public BoardResponseDto getBoard(Long boardIndex) {
        return BoardMapper.toBoardResponseDto(boardRepository.findById(boardIndex)
                .orElseThrow(() -> new CustomException(BoardMessage.BOARD_NOT_FOUND)));
    }

    @Override
    @Transactional
    public BoardResponseDto updateBoard(Long boardIndex, BoardRequestDto boardRequestDto, String userId) {
        Board board = boardRepository.findById(boardIndex)
                .orElseThrow(() -> new CustomException(BoardMessage.BOARD_NOT_FOUND));

        isWriter(board,userId);
        board.updateBoard(boardRequestDto);

        return BoardMapper.toBoardResponseDto(board);
    }

    // jpa 변경감지
    @Override
    @Transactional
    public void deleteBoard(Long boardIndex, String userId) {
        Board board = boardRepository.findById(boardIndex)
                .orElseThrow(() -> new CustomException(BoardMessage.BOARD_NOT_FOUND));

        isWriter(board,userId);

        if (board.isBoardDeleteFlag()) {
            log.warn("[BOARD DELETE 실패] 이미 삭제된 게시글입니다. boardIndex={}", boardIndex);
            throw new CustomException(BoardMessage.ALREADY_DELETED);
        }
        // 스프링 배치 + 스케쥴러 + deleteFlag 이용해서 게시글 지우기
        board.markAsDeleted();
    }

    @Override
    public Boolean isWriter(Board board, String userId) {

        if (board.getUser().getUserId().equals(userId)) {
            return true;
        }
        log.warn("[게시글 작성자 확인 실패] userId={} , boardIndex={} ", userId, board.getBoardIndex());
        throw new CustomException(BoardMessage.WRONG_WRITER);
    }
}
