package com.yh.blogserver.entity;

import com.yh.blogserver.dto.request.BoardRequestDto;
import com.yh.blogserver.dto.request.BoardUpdateRequestDto;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer", referencedColumnName = "userIndex")
    private User user;
    private String boardTitle;
    private String boardContents;
    private String thumbnailUrl;

    @Column(updatable = false)
    private LocalDateTime boardCreatedTime;
    private long boardViewCnt;

    private boolean boardDeleteFlag;
    private LocalDateTime boardDeletedAt;

    @PrePersist
    private void prePersist(){
        this.boardCreatedTime = LocalDateTime.now();
        this.boardViewCnt = 0L;
        this.boardDeleteFlag = false;
    }

    public void markAsDeleted() {
        this.boardDeleteFlag = true;
        this.boardDeletedAt = LocalDateTime.now();
    }
    public void markAsActive() {
        this.boardDeleteFlag = false;
    }

    public void updateBoard(BoardUpdateRequestDto boardUpdateRequestDto) {

        if (boardUpdateRequestDto.boardTitle() != null) {
            this.boardTitle = boardUpdateRequestDto.boardTitle();
        }

        if (boardUpdateRequestDto.boardContents() != null) {
            this.boardContents = boardUpdateRequestDto.boardContents();
        }

        if (boardUpdateRequestDto.thumbnailUrl() != null) {
            this.thumbnailUrl = boardUpdateRequestDto.thumbnailUrl();
        }
    }


}
