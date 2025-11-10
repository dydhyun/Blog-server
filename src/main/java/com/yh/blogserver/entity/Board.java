package com.yh.blogserver.entity;

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

    public void markAsDeleted() {
        this.boardDeleteFlag = true;
    }

    @PrePersist
    private void prePersist(){
        this.boardCreatedTime = LocalDateTime.now();
        this.boardViewCnt = 0L;
        this.boardDeleteFlag = false;
    }
}
