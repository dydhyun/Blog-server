package com.yh.blogserver.service.scheduler;

import com.yh.blogserver.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class UserCleanupService {

    private final UserRepository userRepository;

    public UserCleanupService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Scheduled(cron = "0 */5 * * * *", zone = "Asia/Seoul") // 임시로 5분마다 스케줄링 실행
    @Transactional
    public void deleteExpiredUser(){
        log.info("[USER CLEANUP] deleteExpiredUser 실행");
        LocalDateTime expiredTime = LocalDateTime.now().minusDays(7);

        int deleteCount = userRepository.deleteExpiredUser(expiredTime);

        if (deleteCount > 0){
            log.info("[USER CLEANUP] 삭제된 유저 수 : {}", deleteCount);
        }

    }
}
