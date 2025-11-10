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

    @PrePersist
    private void prePersist(){
        this.isAdmin = false;
        this.createdDate = LocalDateTime.now();
    }
}
