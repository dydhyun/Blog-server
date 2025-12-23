package com.yh.blogserver.controller.blog;

import com.yh.blogserver.dto.response.ResponseDto;
import com.yh.blogserver.dto.response.UserResponseDto;
import com.yh.blogserver.service.user.UserService;
import com.yh.blogserver.util.message.ResponseMessage;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Bolg API", description = "블로그, 게시글 조회 관련 API")
@RestController
@RequestMapping("/blogs")
public class BlogController {

    private final UserService userService;

    public BlogController(UserService userService) {
        this.userService = userService;
    }


    // Jmeter
    @GetMapping("/{userId}")
    public ResponseEntity<ResponseDto<UserResponseDto>> getUser(@PathVariable String userId){

        UserResponseDto userResponseDto = userService.getUserByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(userResponseDto, ResponseMessage.OK.message(), HttpStatus.OK.value()));
    }


}
