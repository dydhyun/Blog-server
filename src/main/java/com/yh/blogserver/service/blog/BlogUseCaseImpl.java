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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class BlogUseCaseImpl implements BlogUseCase {

    private final UserService userService;
    private final BoardService boardService;

    public BlogUseCaseImpl(UserService userService, BoardService boardService) {
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

        BlogHeaderDto header = userService.getBlogHeader(userId);
        log.info("[getBlogHeader 요청] header = {}", header);

        return header;
    }

    @Override
    public Page<BlogBoardSummaryDto> getBlogBoards(String userId, Pageable pageable) {

        Page<BlogBoardSummaryDto> summaries = boardService.getBoardSummariesByUser(userId, pageable);
        log.info("[getBlogBoards 요청] summaries = {}", summaries);

        return summaries;
    }

    @Override
    public BlogResponseDto getUserBlog(String userId, Pageable pageable) {

        BlogHeaderDto header = getBlogHeader(userId);
        Page<BlogBoardSummaryDto> summaries = getBlogBoards(userId, pageable);

        BlogResponseDto blogResponseDto = new BlogResponseDto(header, summaries);
        log.info("[getUserBlog 요청] blogResponse = {}", blogResponseDto);

        return blogResponseDto;
    }

}
