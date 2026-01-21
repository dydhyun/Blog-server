package com.yh.blogserver.repository.board;

import com.yh.blogserver.dto.response.BlogBoardSummaryDto;
import com.yh.blogserver.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;

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


    @Query("""
        select new com.yh.blogserver.dto.response.BlogBoardSummaryDto(
            b.boardIndex,
            b.user.userId,
            b.boardTitle,
            b.thumbnailUrl,
            b.boardCreatedTime,
            b.boardViewCnt
        )
        from Board b
        where b.boardDeleteFlag = false
        order by b.boardCreatedTime desc
    """)
    List<BlogBoardSummaryDto> findNewestBoards(Pageable pageable);

    }
