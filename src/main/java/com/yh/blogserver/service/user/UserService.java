package com.yh.blogserver.service.user;

import com.yh.blogserver.dto.request.UserRequestDto;
import com.yh.blogserver.dto.request.UserUpdateRequestDto;
import com.yh.blogserver.dto.response.BlogHeaderDto;
import com.yh.blogserver.dto.response.PageResponse;
import com.yh.blogserver.dto.response.UserResponseDto;
import com.yh.blogserver.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Set;

public interface UserService {

    void userIdCheck(String userId);

    void userPwCheck(String userPw);

    void userNicknameCheck(String userNickname);

    UserResponseDto join(UserRequestDto userRequestDto);

//    UserResponseDto login(UserRequestDto userRequestDto);

    User getUserOrThrow(String userId);

    UserResponseDto getUserByUserId(String UserId);

    String authenticatedUser(String token);

//    boolean isAdmin(String userId);

    Map<String, BlogHeaderDto> getBlogHeadersByUserIds(Set<String> userIds);

    BlogHeaderDto getBlogHeader(String userId);

    void updateMyPage(String userId, UserUpdateRequestDto userUpdateRequestDto);

    void deleteAccount(String userId);

    PageResponse<UserResponseDto> getDeletedUsers(Pageable pageable);

    void restoreAccount(String userId);
}
