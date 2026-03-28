package com.yh.blogserver.service.cleanup;

import com.yh.blogserver.repository.board.BoardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;

@Slf4j
@Service
public class BoardCleanupService extends AbstractCleanupService {

    private final BoardRepository boardRepository;

    public BoardCleanupService(BoardRepository boardRepository, TransactionTemplate transactionTemplate) {
        super(transactionTemplate);
        this.boardRepository = boardRepository;
    }

    public void deleteExpiredBoards() {
        cleanup("BOARD");
    }

    @Override
    protected int deleteChunk(LocalDateTime expiredTime){
        return boardRepository.deleteExpiredBoard(expiredTime, CHUNK_SIZE);
    }

}