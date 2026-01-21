package com.yh.blogserver.service.blog;

import com.yh.blogserver.dto.response.BlogBoardSummaryDto;
import com.yh.blogserver.dto.response.BlogHeaderDto;
import com.yh.blogserver.dto.response.BlogResponseDto;
import com.yh.blogserver.dto.response.MainBlogCardDto;
import com.yh.blogserver.service.board.BoardService;
import com.yh.blogserver.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BlogServiceImpl implements BlogService{

    private final UserService userService;
    private final BoardService boardService;

    public BlogServiceImpl(UserService userService, BoardService boardService) {
        this.userService = userService;
        this.boardService = boardService;
    }

    @Override
    public List<MainBlogCardDto> getNewestBlogs() {
//        log.info("[BlogService getNewestBlogs] 최신글 가져오기 실행");

        List<BlogBoardSummaryDto> blogBoardSummaries = boardService.getNewestBoards(3);
        log.info("[blogBoardSummaries = {} ]",blogBoardSummaries);

        Set<String> userIds = blogBoardSummaries.stream()
                .map(BlogBoardSummaryDto::userId)
                .collect(Collectors.toSet());

        Map<String, BlogHeaderDto> userMap = userService.getBlogHeadersByUserIds(userIds);
        log.info("[userMap = {} ]", userMap);

        List<MainBlogCardDto> cards = blogBoardSummaries.stream()
                .map(boardSummary -> new MainBlogCardDto(
                        userMap.get(boardSummary.userId()),
                        boardSummary
                ))
                .toList();

        return cards;
    }

    @Override
    public BlogHeaderDto getBlogHeader(String userId) {
        return userService.getBlogHeader(userId);
    }

    @Override
    public Page<BlogBoardSummaryDto> getBlogBoards(String userId, Pageable pageable) {
        return boardService.getBoardSummariesByUser(userId, pageable);
    }

    @Override
    public BlogResponseDto getUserBlog(String userId, Pageable pageable) {
        return null;
    }

}
