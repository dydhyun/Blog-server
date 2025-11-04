package com.yh.blogserver.service.user;

import com.yh.blogserver.dto.UserDto;

import java.util.Map;

public interface UserService {

    Map<String, String> userIdCheck(String userId);

    Map<String, String> userPwCheck(String userPw);

    Map<String, String> userNicknameCheck(String userNickname);

    UserDto join(UserDto userDto);

    UserDto login(UserDto userDto);

    UserDto getUserByUserId(String UserId);
}
