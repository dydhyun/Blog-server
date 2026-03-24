package com.yh.blogserver.service.scheduler;

import com.yh.blogserver.service.cleanup.BoardCleanupService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class BoardCleanupScheduler {

    private final BoardCleanupService boardCleanupService;

    public BoardCleanupScheduler(BoardCleanupService boardCleanupService) {
        this.boardCleanupService = boardCleanupService;
    }

    @Scheduled(cron = "0 0 1 1 * *", zone = "Asia/Seoul") // 매월 1일 스케줄링 실행
    public void run(){
        boardCleanupService.deleteExpiredBoards();
    }

}
