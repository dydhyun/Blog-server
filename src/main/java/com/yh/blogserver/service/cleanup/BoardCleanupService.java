package com.yh.blogserver.service.cleanup;

import com.yh.blogserver.repository.board.BoardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class BoardCleanupService {

    private final BoardRepository boardRepository;

    public BoardCleanupService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Transactional
    public void deleteExpiredBoards() {
        log.info("[Board Cleanup] deleteExpiredBoard 실행");
        LocalDateTime expiredTime = LocalDateTime.now().minusDays(30);

        int deleteCount = boardRepository.deleteExpiredBoard(expiredTime);

        if (deleteCount > 0) {
            log.info("[Board Cleanup] 삭제된 게시글 수 = {} ", deleteCount);
        }
    }

}