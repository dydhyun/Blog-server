package com.yh.blogserver.service.mypage;

import com.yh.blogserver.dto.response.UserResponseDto;
import com.yh.blogserver.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class MyPageUseCaseImpl implements MyPageUseCase {

    private final UserService userService;

    public MyPageUseCaseImpl(UserService userService) {
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponseDto getMyPage(String userId) {
        return userService.getUserByUserId(userId);
    }

}
