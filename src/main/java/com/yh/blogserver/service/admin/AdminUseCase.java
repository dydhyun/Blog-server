package com.yh.blogserver.service.admin;

import com.yh.blogserver.dto.response.PageResponse;
import com.yh.blogserver.dto.response.UserResponseDto;
import org.springframework.data.domain.Pageable;

public interface AdminUseCase {

    PageResponse<UserResponseDto> getDeletedUsers(Pageable pageable);

    void deleteUser(String userId);
}
