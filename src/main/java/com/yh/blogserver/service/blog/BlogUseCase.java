package com.yh.blogserver.service.blog;

import com.yh.blogserver.dto.response.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BlogUseCase {
    List<MainBlogCardDto> getNewestBlogs();

    BlogHeaderDto getBlogHeader(String userId);

    PageResponse<BlogBoardSummaryDto> getBlogBoards(String userId, Pageable pageable);

    BlogResponseDto getUserBlog(String userId, Pageable pageable);
}
