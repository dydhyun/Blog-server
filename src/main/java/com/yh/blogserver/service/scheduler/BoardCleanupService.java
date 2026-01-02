package com.yh.blogserver.service.scheduler;

import com.yh.blogserver.repository.board.BoardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
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

    @Scheduled(cron = "0 0/30 * * * *")// 정각/30분마다 실행
    @Transactional
    public void deleteExpiredBoard(){
//        log.info("[Board Cleanup] deleteExpiredBoard 실행");
        LocalDateTime expiredTime = LocalDateTime.now().minusDays(7);

        int deleteCount = boardRepository.deleteExpiredBoard(expiredTime);

        if(deleteCount > 0){
            log.info("[Board Cleanup] 삭제된 게시글 수 = {} ", deleteCount);
        }
    }
}
