package com.yh.blogserver.service.mypage;

import com.yh.blogserver.dto.request.UserRequestDto;
import com.yh.blogserver.dto.request.UserUpdateRequestDto;
import com.yh.blogserver.dto.response.UserResponseDto;

public interface MyPageUseCase {
    UserResponseDto getMyPage(String userId);

    void updateMyPage(String userId, UserUpdateRequestDto userUpdateRequestDto);

    void deleteMyAccount(String userId);
}
