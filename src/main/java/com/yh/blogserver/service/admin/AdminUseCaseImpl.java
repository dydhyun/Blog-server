package com.yh.blogserver.service.admin;

import com.yh.blogserver.dto.response.PageResponse;
import com.yh.blogserver.dto.response.UserResponseDto;
import com.yh.blogserver.service.board.BoardService;
import com.yh.blogserver.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AdminUseCaseImpl implements AdminUseCase{

    private final UserService userService;
    private final BoardService boardService;

    public AdminUseCaseImpl(UserService userService, BoardService boardService) {
        this.userService = userService;
        this.boardService = boardService;
    }

    // 정렬기능 추가
    @Override
    public PageResponse<UserResponseDto> getDeletedUsers(Pageable pageable) {
        return userService.getDeletedUsers(pageable);
    }

    @Override
    public void deleteUser(String userId) {
        userService.deleteAccount(userId);
    }

    @Override
    public void restoreUser(String userId) {
        userService.restoreAccount(userId);
    }

    @Override
    public void deleteBoard(Long boardId) {
        boardService.deleteBoardByAdmin(boardId);
    }

    @Override
    public void restoreBoard(Long boardId) {
        boardService.restoreBoard(boardId);
    }
}
