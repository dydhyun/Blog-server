package com.yh.blogserver.service.scheduler;

import com.yh.blogserver.repository.auth.RefreshTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class TokenCleanupService {

    private final RefreshTokenRepository refreshTokenRepository;

    public TokenCleanupService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Scheduled(cron = "0 0 1 * * *") // 매일 새벽 1시
    @Transactional
    public void deleteExpiredTokens(){
        int deleteCount =
                refreshTokenRepository.deleteExpiredTokens(LocalDateTime.now());


        if(deleteCount > 0){
            log.info("[Token Cleanup] 삭제된 토큰 수 = {} ", deleteCount);
        }
    }
}
