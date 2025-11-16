package com.yh.blogserver.entity;

import com.yh.blogserver.dto.request.BoardRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@Getter
public class Board {
    protected Board() {} // jpa 자체 에서만 사용하는 기본생성자.

    public Board(User user, String title, String contents) {
        this.user = user;
        this.boardTitle = title;
        this.boardContents = contents;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardIndex;

    @ManyToOne
    @JoinColumn(name = "writer", referencedColumnName = "userIndex")
    private User user;
    private String boardTitle;
    private String boardContents;

    @Column(updatable = false)
    private LocalDateTime boardCreatedTime;
    private Long boardViewCnt;

    private boolean boardDeleteFlag;

    @PrePersist
    private void prePersist(){
        this.boardCreatedTime = LocalDateTime.now();
        this.boardViewCnt = 0L;
        this.boardDeleteFlag = false;
    }

    public void markAsDeleted() {
        this.boardDeleteFlag = true;
    }

    public void updateBoard(BoardRequestDto boardRequestDto){
        this.boardTitle = boardRequestDto.boardTitle();
        this.boardContents = boardRequestDto.boardContents();
    }

}
