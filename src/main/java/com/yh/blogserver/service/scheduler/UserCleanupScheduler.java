package com.yh.blogserver.service.scheduler;

import com.yh.blogserver.service.cleanup.UserCleanupService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class UserCleanupScheduler {

    private final UserCleanupService userCleanupService;

    public UserCleanupScheduler(UserCleanupService userCleanupService) {
        this.userCleanupService = userCleanupService;
    }

    @Scheduled(cron = "0 0 1 1 * *", zone = "Asia/Seoul") // 매월 1일 스케줄링 실행
    public void run(){
        userCleanupService.deleteExpiredUsers();
    }

}
