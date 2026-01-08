package com.yh.blogserver.repository.board;

import com.yh.blogserver.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Modifying
    @Query("""
        DELETE FROM Board b
        WHERE b.boardDeleteFlag = true
        AND b.boardDeletedAt <= :expiredTime
""")
    int deleteExpiredBoard(LocalDateTime expiredTime);

    Page<Board> findByBoardTitleContainingAndBoardDeleteFlagFalse(String keyword, Pageable pageable);

    Page<Board> findByBoardContentsContainingAndBoardDeleteFlagFalse(String keyword, Pageable pageable);

    Page<Board> findByUser_NicknameAndBoardDeleteFlagFalse(String keyword, Pageable pageable);
}
