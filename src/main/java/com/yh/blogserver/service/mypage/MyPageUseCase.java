package com.yh.blogserver.service.mypage;

import com.yh.blogserver.dto.response.UserResponseDto;

public interface MyPageUseCase {
    UserResponseDto getMyPage(String userId);
}
