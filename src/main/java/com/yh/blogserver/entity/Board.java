package com.yh.blogserver.entity;

import com.yh.blogserver.dto.BoardDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
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

    public void markAsDeleted() {
        this.boardDeleteFlag = true;
    }

    @PrePersist
    private void prePersist(){
        this.boardCreatedTime = LocalDateTime.now();
        this.boardViewCnt = 0L;
        this.boardDeleteFlag = false;
    }

    public BoardDto toDto(){
        return BoardDto.builder()
                .boardIndex(this.boardIndex)
                .user(this.user)
                .boardTitle(this.boardTitle)
                .boardContents(this.boardContents)
                .boardCreatedTime(this.boardCreatedTime)
                .boardViewCnt(this.boardViewCnt)
                .boardDeleteFlag(this.boardDeleteFlag)
                .build();
    }

}
