package com.yh.blogserver.controller.blog;

import com.yh.blogserver.dto.response.*;
import com.yh.blogserver.service.blog.BlogUseCase;
import com.yh.blogserver.util.message.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Bolg API", description = "블로그, 사용자 + 게시글 조회 관련 API")
@RestController
@RequestMapping("/blogs")
public class BlogController {

    private static final Logger log = LoggerFactory.getLogger(BlogController.class);
    private final BlogUseCase blogService;

    public BlogController(BlogUseCase blogService) {
        this.blogService = blogService;
    }

    // Jmeter
    @Operation(summary = "블로그 조회 API",
            description = "메인에서 사용 될 블로그(유저 + 최신 게시글) 조회 API"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "조회 실패")
    })
    @GetMapping()
    public ResponseEntity<ResponseDto<List<MainBlogCardDto>>> getBlogs(){
//        log.info("[MAIN 최신 3개의 글 블로그 조회 요청]");

        List<MainBlogCardDto> newestBlogs = blogService.getNewestBlogs();

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(newestBlogs, ResponseMessage.OK.message(),HttpStatus.OK.value()));
    }

    @Operation(summary = "특정 유저 블로그 조회",
            description = """
                    유저 아이디를 PathVariable로 받아 특정유저 블로그를 조회합니다.
                    블로그 주인 정보, 게시글 목록(페이징), 게시글 수 반환.
                    *블로그 주인 정보와 게시글 목록은 데이터 변경 주기가 다릅니다.*
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "조회 실패"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 블로그")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<ResponseDto<BlogResponseDto>> getUserBlog(@PathVariable String userId, Pageable pageable) {
        log.info("[GET USERS BLOG 요청] blogUserId={}", userId);

        BlogResponseDto blogResponseDto = blogService.getUserBlog(userId, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(blogResponseDto, ResponseMessage.OK.message(), HttpStatus.OK.value()));
    }

    // SPA 최적화
    @Operation(summary = "고정된 헤더로 사용 될 특정 유저 정보 조회.",
            description = "유저 아이디를 PathVariable로 받아 특정 블로그의 유저 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "조회 실패"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 블로그")
    })
    @GetMapping("/{userId}/header")
    public ResponseEntity<ResponseDto<BlogHeaderDto>> getBlogHeader(@PathVariable String userId) {
        log.info("[GET BLOG HEADER 요청] blogUserId={}", userId);

        BlogHeaderDto headerDto = blogService.getBlogHeader(userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(headerDto, ResponseMessage.OK.message(), HttpStatus.OK.value()));
    }

    @Operation(summary = "재요청이 많은 바디로 들어갈 유저의 글 목록 조회.",
            description = "유저 아이디를 PathVariable로 받아 특정 블로그의 게시글 목록을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "조회 실패"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 블로그")
    })
    @GetMapping("/{userId}/boards")
    public ResponseEntity<ResponseDto<PageResponse<BlogBoardSummaryDto>>> getBlogBoards(@PathVariable String userId,
                                                                                Pageable pageable) {
        log.info("[GET BLOG BOARDS 요청] blogUserId={}", userId);

        PageResponse<BlogBoardSummaryDto> boardSummaries = blogService.getBlogBoards(userId, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(boardSummaries, ResponseMessage.OK.message(), HttpStatus.OK.value()));
    }


}
