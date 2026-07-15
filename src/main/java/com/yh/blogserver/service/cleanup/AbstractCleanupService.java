package com.yh.blogserver.service.cleanup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
public abstract class AbstractCleanupService {

    protected static final int CHUNK_SIZE = 5000;
    private final TransactionTemplate transactionTemplate;

    protected AbstractCleanupService(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    protected final void cleanup(String domainName){
        log.info("[{} CLEANUP] 실행", domainName);
        LocalDateTime expiredTime = LocalDateTime.now().minusDays(0);

        int totalDeleted = 0;

        while (true){
            int deleted = Optional.ofNullable(
                    transactionTemplate.execute(status -> deleteChunk(expiredTime))
            ).orElse(0);

            if (deleted == 0){
                break;
            }
            totalDeleted += deleted;
            log.info("삭제 진행중. chunk 삭제 수 = {}", deleted);
        }
        log.info("총 삭제 수 = {}", totalDeleted);
    }

    protected abstract int deleteChunk(LocalDateTime expiredTime);

}
