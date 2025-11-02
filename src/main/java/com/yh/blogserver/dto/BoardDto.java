package com.yh.blogserver.dto;

import com.yh.blogserver.entity.Board;
import com.yh.blogserver.entity.User;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {

    public Long boardIndex;
    public User user;
    public String boardTitle;
    public String boardContents;
    public LocalDateTime boardCreatedTime;
    public Long boardViewCnt;

    public Board toEntity(){
        return Board.builder()
                .boardIndex(this.boardIndex)
                .user(this.user)
                .boardTitle(this.boardTitle)
                .boardContents(this.boardContents)
                .boardCreatedTime(this.boardCreatedTime)
                .boardViewCnt(this.boardViewCnt)
                .build();
    }

}
