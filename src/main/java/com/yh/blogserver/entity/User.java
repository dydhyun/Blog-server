package com.yh.blogserver.entity;

import com.yh.blogserver.dto.request.UserRequestDto;
import com.yh.blogserver.dto.request.UserUpdateRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    private String profileImageUrl;
    private String description;

    private Boolean isAdmin;

    @Column(updatable = false)
    private LocalDateTime userCreatedTime;
    private boolean userDeleteFlag;
    private LocalDateTime userDeletedAt;

    public void markAsDeleted() {
        this.userDeleteFlag = true;
        this.userDeletedAt = LocalDateTime.now();
    }
    public void markAsActive() {
        this.userDeleteFlag = false;
    }
    // Lombok 의 builder , AllArgsConstructor 로 객체 생성시 기본필드 초기값 무시됨.
    // @PrePersist 는 JPA 가 persist() 직전에 실행해서 안전하게 모든 생성 방식에서 빠진 필드값을 채워넣을 수 있음.
    @PrePersist
    private void prePersist(){
        this.isAdmin = false;
        this.userCreatedTime = LocalDateTime.now();
        this.userDeleteFlag = false;
    }

    public void update(UserUpdateRequestDto dto) {

        if (dto.nickname() != null && !dto.nickname().isEmpty()) {
            this.nickname = dto.nickname();
        }

        if (dto.email() != null && !dto.email().isBlank()) {
            this.email = dto.email();
        }

        if (dto.pNumber() != null && !dto.pNumber().isBlank()) {
            this.pNumber = dto.pNumber();
        }

        if (dto.address() != null) {
            this.address = dto.address();
        }

        if (dto.addressDetail() != null) {
            this.addressDetail = dto.addressDetail();
        }

        if (dto.description() != null) {
            this.description = dto.description();
        }

        if (dto.profileImageUrl() != null) {
            this.profileImageUrl = dto.profileImageUrl();
        }
    }

    public void changePassword(String encodedPw){
        this.userPw = encodedPw;
    }

}
