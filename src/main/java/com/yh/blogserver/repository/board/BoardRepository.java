package com.yh.blogserver.repository.board;

import com.yh.blogserver.dto.response.BlogBoardSummaryDto;
import com.yh.blogserver.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Modifying
    @Query(value = """
        DELETE FROM board
        WHERE board_delete_flag = true
        AND board_deleted_at <= :expiredTime
        LIMIT :limit
    """, nativeQuery = true)
    int deleteExpiredBoard(@Param("expiredTime") LocalDateTime expiredTime, @Param("limit") int limit);

    @Modifying
    @Query(value = """
        DELETE FROM Board b
        WHERE b.boardDeleteFlag = true
        AND b.boardDeletedAt <= :expiredTime
    """)
    int deleteBoardTest(@Param("expiredTime") LocalDateTime expiredTime);

    @Query(
    value = """
    select b
    from Board b
    join fetch b.user
    where b.boardTitle like %:keyword%
    and b.boardDeleteFlag = false
    """,
    countQuery = """
    select count(b)
    from Board b
    where b.boardTitle like %:keyword%
    and b.boardDeleteFlag = false
    """)
    Page<Board> searchBoardTitleWithFetchJoin(String keyword, Pageable pageable);
//    Page<Board> findByBoardTitleContainingAndBoardDeleteFlagFalse(String keyword, Pageable pageable);

    @EntityGraph(attributePaths = "user")
    Page<Board> findByBoardContentsContainingAndBoardDeleteFlagFalse(String keyword, Pageable pageable);

//    @Query("""
//        select b
//        from Board b
//        where b.user.nickname = :keyword
//        and b.boardDeleteFlag = false
//    """)
//    @Query("""
//            select b
//            from Board b
//            join fetch b.user
//            where b.user.nickname like %:keyword%
//            and b.boardDeleteFlag = false
//        """)
//    Page<Board> findByUser_NicknameAndBoardDeleteFlagFalse(String keyword, Pageable pageable);
    Page<Board> findByUser_NicknameContainingAndBoardDeleteFlagFalse(String keyword, Pageable pageable);


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
        where b.user.userId = :userId
        and b.boardDeleteFlag = false
        order by b.boardCreatedTime desc
    """)
    Page<BlogBoardSummaryDto> findBoardSummariesByUserId(@Param("userId") String userId, Pageable pageable);

    @Modifying
    @Query("update Board b set b.boardViewCnt = b.boardViewCnt + 1 where b.boardIndex = :boardIndex")
    void increaseBoardViewCount(@Param("boardIndex") Long boardIndex);

    @EntityGraph(attributePaths = {"user"})
    Page<Board> findByBoardDeleteFlagTrue(Pageable pageable);
}
