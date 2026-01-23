package com.yh.blogserver.service.mypage;

import com.yh.blogserver.dto.request.UserUpdateRequestDto;
import com.yh.blogserver.dto.response.UserResponseDto;
import com.yh.blogserver.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MyPageUseCaseImpl implements MyPageUseCase {

    private final UserService userService;

    public MyPageUseCaseImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserResponseDto getMyPage(String userId) {
        return userService.getUserByUserId(userId);
    }

    @Override
    public void updateMyPage(String userId, UserUpdateRequestDto userUpdateRequestDto) {
        userService.updateMyPage(userId, userUpdateRequestDto);
    }

}
