//package com.yh.blogserver.dto;
//
//import com.yh.blogserver.entity.User;
//import lombok.*;
//
//import java.time.LocalDateTime;
//
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//public class UserDto {
//    private Long userIndex;
//    private String userId;
//    private String userPw;
//    private String username;
//    private String nickname;
//    private String address;
//    private String addressDetail;
//    private String pNumber;
//    private String email;
//    private Boolean isAdmin;
//    private LocalDateTime createdDate;
//
//    public User toEntity() {
//        return User.builder()
//                .userIndex(this.userIndex)
//                .userId(this.userId)
//                .userPw(this.userPw)
//                .username(this.username)
//                .nickname(this.nickname)
//                .address(this.address)
//                .addressDetail(this.addressDetail)
//                .pNumber(this.pNumber)
//                .email(this.email)
//                .isAdmin(this.isAdmin)
//                .createdDate(this.createdDate)
//                .build();
//    }
//
//}
