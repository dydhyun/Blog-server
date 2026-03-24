package com.yh.blogserver.service.cleanup;

import com.yh.blogserver.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class UserCleanupService {

    private final UserRepository userRepository;
    private static final int CHUNK_SIZE = 1000;

    public UserCleanupService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void deleteExpiredUsers(){
        log.info("[USER CLEANUP] deleteExpiredUser 실행");
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
        return userRepository.deleteExpiredUser(expiredTime, CHUNK_SIZE);
    }

}