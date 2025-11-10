package com.yh.blogserver.mapper;

import com.yh.blogserver.dto.request.UserRequestDto;
import com.yh.blogserver.dto.response.UserResponseDto;
import com.yh.blogserver.entity.User;

public class UserMapper {

    private UserMapper(){
    }

    public static UserResponseDto toUserResponseDto(User user) {
        return UserResponseDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .address(user.getAddress())
                .addressDetail(user.getAddressDetail())
                .email(user.getEmail())
                .createdDate(user.getCreatedDate())
                .build();
    }

    public static UserRequestDto toUserRequestDto(User user) {
        return UserRequestDto.builder()
                .userIndex(user.getUserIndex())
                .userId(user.getUserId())
                .userPw(user.getUserPw())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .address(user.getAddress())
                .addressDetail(user.getAddressDetail())
                .pNumber(user.getPNumber())
                .email(user.getEmail())
                .createdDate(user.getCreatedDate())
                .build();
    }

    public static User fromDto(UserRequestDto dto) {
        return User.builder()
                .userIndex(dto.userIndex())
                .userId(dto.userId())
                .userPw(dto.userPw())
                .username(dto.username())
                .nickname(dto.nickname())
                .address(dto.address())
                .addressDetail(dto.addressDetail())
                .pNumber(dto.pNumber())
                .email(dto.email())
                .createdDate(dto.createdDate())
                .build();
    }

}
