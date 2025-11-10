package com.yh.blogserver.repository.board;

import com.yh.blogserver.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    void deleteByBoardIndex(Long boardIndex);

    boolean existsByBoardIndexAndUser_UserId(Long boardIndex, String userId);
}
