package com.yh.blogserver.service.user;

import com.yh.blogserver.dto.request.UserRequestDto;
import com.yh.blogserver.dto.response.UserResponseDto;
import com.yh.blogserver.entity.User;

import java.util.Map;

public interface UserService {

    Map<String, String> userIdCheck(String userId);

    Boolean userPwCheck(String userPw);

    Map<String, String> userNicknameCheck(String userNickname);

    UserResponseDto join(UserRequestDto userRequestDto);

    UserResponseDto login(UserRequestDto userRequestDto);

    UserResponseDto getUserByUserId(String UserId);

    User getUserEntityByUserId(String userId);

    String authenticatedUser(String token);

}
