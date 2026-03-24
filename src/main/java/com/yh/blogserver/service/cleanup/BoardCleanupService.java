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
    private static final int CHUNK_SIZE = 1000;

    public BoardCleanupService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Transactional
    public void deleteExpiredBoards() {
        log.info("[Board CLEANUP] deleteExpiredBoard 실행");
        LocalDateTime expiredTime = LocalDateTime.now().minusDays(30);

        int totalDeleted = 0;

        while (true){
            int deleted = deleteChunk(expiredTime);

            if (deleted == 0){
                break;
            }

            totalDeleted += deleted;
            log.info("삭제 진행중. chunk 삭제 수 = {}", deleted);
        }
        log.info("총 삭제 수 = {}", totalDeleted);
    }

    private int deleteChunk(LocalDateTime expiredTime){
        return boardRepository.deleteExpiredBoard(expiredTime, CHUNK_SIZE);
    }

}