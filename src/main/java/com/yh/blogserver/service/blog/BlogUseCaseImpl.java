package com.yh.blogserver.service.blog;

import com.yh.blogserver.dto.response.*;
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
        log.debug("newest blog summaries count={}", blogBoardSummaries.size());

        Map<String, BlogHeaderDto> headers = loadBlogHeaders(blogBoardSummaries);

        List<MainBlogCardDto> cards = combineSummariesAndHeaders(blogBoardSummaries, headers);

        return cards;
    }

    private Map<String, BlogHeaderDto> loadBlogHeaders(List<BlogBoardSummaryDto> blogBoardSummaries){

        Set<String> userIds = blogBoardSummaries.stream()
                .map(BlogBoardSummaryDto::userId)
                .collect(Collectors.toSet());

        Map<String, BlogHeaderDto> headers = userService.getBlogHeadersByUserIds(userIds);
        log.debug("headers loaded count={}", headers.size());

        return headers;
    }

    private List<MainBlogCardDto> combineSummariesAndHeaders(List<BlogBoardSummaryDto> blogBoardSummaries, Map<String, BlogHeaderDto> headers){

        List<MainBlogCardDto> cards = blogBoardSummaries.stream()
                .map(boardSummary -> new MainBlogCardDto(
                        headers.get(boardSummary.userId()),
                        boardSummary
                ))
                .toList();
        log.debug("main blog cards count={}", cards.size());

        return cards;
    }

    @Override
    public BlogHeaderDto getBlogHeader(String userId) {

        BlogHeaderDto header = userService.getBlogHeader(userId);
        log.info("[getBlogHeader] userId={}", userId);
        log.debug("header={}", header);

        return header;
    }

    @Override
    public PageResponse<BlogBoardSummaryDto> getBlogBoards(String userId, Pageable pageable) {

        Page<BlogBoardSummaryDto> summaries = boardService.getBoardSummariesByUser(userId, pageable);

        log.info("[getBlogBoards] userId={}, page={}, size={}, total={}",
                userId,
                summaries.getNumber(),
                summaries.getSize(),
                summaries.getTotalElements());

        log.debug("summaries.getNumberOfElements = {}", summaries.getNumberOfElements());

        return PageResponse.from(summaries);
    }

    @Override
    public BlogResponseDto getUserBlog(String userId, Pageable pageable) {

        BlogHeaderDto header = getBlogHeader(userId);
        PageResponse<BlogBoardSummaryDto> summaries = getBlogBoards(userId, pageable);

        BlogResponseDto blogResponseDto = new BlogResponseDto(header, summaries);
        log.info("[getUserBlog] userId={}", userId);
//        log.debug("blogResponse={}", blogResponseDto);

        return blogResponseDto;
    }

}
