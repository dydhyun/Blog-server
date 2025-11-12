package com.yh.blogserver.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "userSeqGenerator"
    )
    private Long userIndex;

    @Column(unique = true)
    private String userId;
    private String userPw;
    private String username;

    @Column(unique = true)
    private String nickname;
    private String address;
    private String addressDetail;
    private String pNumber;
    private String email;

    private Boolean isAdmin;

    @Column(updatable = false)
    private LocalDateTime createdDate;
    private Boolean userDeleteFlag;

    public void markAsDeleted() {
        this.userDeleteFlag = true;
    }
    // Lombok 의 builder , AllArgsConstructor 로 객체 생성시 기본필드 초기값 무시됨.
    // @PrePersist 는 JPA 가 persist() 직전에 실행해서 안전하게 모든 생성 방식에서 빠진 필드값을 채워넣을 수 있음.
    @PrePersist
    private void prePersist(){
        this.isAdmin = false;
        this.createdDate = LocalDateTime.now();
        this.userDeleteFlag = false;
    }
}
