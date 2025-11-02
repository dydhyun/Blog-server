package com.yh.blogserver.entitiy;

import com.yh.blogserver.dto.UserDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
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


    public UserDto toDto() {
        return UserDto.builder()
                .userIndex(this.userIndex)
                .userId(this.userId)
                .userPw(this.userPw)
                .username(this.username)
                .nickname(this.nickname)
                .address(this.address)
                .addressDetail(this.addressDetail)
                .pNumber(this.pNumber)
                .email(this.email)
                .isAdmin(this.isAdmin)
                .createdDate(this.createdDate)
                .build();
    }

}
