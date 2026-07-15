package com.yh.blogserver.service.cleanup;

import com.yh.blogserver.repository.board.BoardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
public class BoardTestCleanService {

    private final BoardRepository boardRepository;

    public BoardTestCleanService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Transactional
    public void cleanBoard_notDivide(){

        log.info("[{} CLEANUP] 실행", "비용 측정 용도 청크분리 안한 Board");
        LocalDateTime expiredTime = LocalDateTime.now().minusDays(0);

        int totalDeleted = boardRepository.deleteBoardTest(expiredTime);

        log.info("총 삭제 수 : {}", totalDeleted);
    }

}
