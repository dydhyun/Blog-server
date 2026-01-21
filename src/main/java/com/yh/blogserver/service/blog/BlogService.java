package com.yh.blogserver.service.blog;

import com.yh.blogserver.dto.response.BlogBoardSummaryDto;
import com.yh.blogserver.dto.response.BlogHeaderDto;
import com.yh.blogserver.dto.response.BlogResponseDto;
import com.yh.blogserver.dto.response.MainBlogCardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BlogService {
    List<MainBlogCardDto> getNewestBlogs();

    BlogHeaderDto getBlogHeader(String userId);

    Page<BlogBoardSummaryDto> getBlogBoards(String userId, Pageable pageable);

    BlogResponseDto getUserBlog(String userId, Pageable pageable);
}
