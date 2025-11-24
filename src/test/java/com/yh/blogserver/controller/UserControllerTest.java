package com.yh.blogserver.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.yh.blogserver.config.JwtTokenProvider;
import com.yh.blogserver.controller.user.UserController;
import com.yh.blogserver.dto.request.UserRequestDto;
import com.yh.blogserver.dto.response.UserResponseDto;
import com.yh.blogserver.exception.CustomException;
import com.yh.blogserver.service.user.UserService;
import com.yh.blogserver.util.message.UserMessage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Spring MockMvc를 사용한 UserController 통합 테스트
 * - @WebMvcTest: Controller 레이어만 테스트 (경량화)
 * - MockMvc: HTTP 요청/응답 시뮬레이션
 * - @MockitoBean: Service 계층 모킹 (Spring Boot 3.4.0+ 권장)
 */
// ControllerTest 의 의미 :
// HTTP 요청 -> Controller -> 예외 핸들러 -> ResponseEntity
// JSON 응답 구조가 설계한 대로 나오는가?
// 예외가 올바른 status 로 매핑되는가?
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    ObjectMapper objectMapper;

    private static final String CHECK_MSG_KEY = "checkMessage";

    @Test
    void userIdCheck_success() throws Exception {
        //given
        String userId = "testId1124001";
        Map<String, String> response = Map.of(CHECK_MSG_KEY, UserMessage.AVAILABLE_USER_ID.message());

        //when
        Mockito.when(userService.userIdCheck(userId))
                .thenReturn(response);

        // then
        mockMvc.perform(get("/users/exists/id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("userId",userId))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.data.checkMessage").value(UserMessage.AVAILABLE_USER_ID.message()))
                .andExpect(jsonPath("$.message").value(UserMessage.AVAILABLE_USER_ID.message()))
                .andExpect(jsonPath("$.responseCode").value("200"));
    }

    @Test
    void userIdCheck_fail() throws Exception {
        // given
        String userId = "ayh0123";

        // when
        Mockito.when(userService.userIdCheck(userId))
                .thenThrow(new CustomException(UserMessage.INVALID_USER_ID));

        // then
        mockMvc.perform(get("/users/exists/id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("userId",userId))
                .andExpect(status().is4xxClientError())
                .andDo(print())
//                .andExpect(jsonPath("$.data").value(null))
                .andExpect(jsonPath("$.message").value(UserMessage.INVALID_USER_ID.message()))
                .andExpect(jsonPath("$.responseCode").value(UserMessage.INVALID_USER_ID.code()));
    }

    @Test
    void userNickname_success() throws Exception {
        // given
        String userNickname = "nickname";
        Map<String, String> response = Map.of(CHECK_MSG_KEY, UserMessage.AVAILABLE_USER_NICKNAME.message());

        // when
        Mockito.when(userService.userNicknameCheck(userNickname))
                .thenReturn(response);

        // then
        mockMvc.perform(get("/users/exists/nickname")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("userNickname",userNickname))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.data.checkMessage").value(UserMessage.AVAILABLE_USER_NICKNAME.message()))
                .andExpect(jsonPath("$.message").value(UserMessage.AVAILABLE_USER_NICKNAME.message()))
                .andExpect(jsonPath("$.responseCode").value("200"));
    }

    @Test
    void userNickname_fail() throws Exception {
        // given
        String userNickname = "nicknameFail";

        // when
        Mockito.when(userService.userNicknameCheck(userNickname))
                .thenThrow(new CustomException(UserMessage.INVALID_USER_NICKNAME));

        // then
        mockMvc.perform(get("/users/exists/nickname")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("userNickname",userNickname))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andExpect(jsonPath("$.message").value(UserMessage.INVALID_USER_NICKNAME.message()))
                .andExpect(jsonPath("$.responseCode").value(UserMessage.INVALID_USER_NICKNAME.code()));
    }

    @Test
    void joinTest_success() throws Exception {
        // given
        String userId = "userId123";
        String userPw = "userPswd!23";
        String userName = "유저컨트롤러테스트";
        String userNickname = "userNickname";
        String address = "address";
        String pNum = "01012345678";
        String email = "dydgus625@naver.com";
        UserRequestDto userRequestDto =
                new UserRequestDto(userId, userPw, userName, userNickname, address, null, pNum, email);

        UserResponseDto response =
                new UserResponseDto( 1L, userId, userName, userNickname, address, null, pNum, email, false, LocalDateTime.now());

        // when
        Mockito.when(userService.join(any(UserRequestDto.class)))
                .thenReturn(response);

        // then
        mockMvc.perform(post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.data.userId").value(response.userId()))
                .andExpect(jsonPath("$.data.username").value(response.username()))
                .andExpect(jsonPath("$.data.email").value(response.email()))
                .andExpect(jsonPath("$.message").value(UserMessage.JOIN_SUCCESS.message()))
                .andExpect(jsonPath("$.responseCode").value(HttpStatus.CREATED.value()));
    }

    @Test
    void joinTest_fail() throws Exception{
        // given
        String userId = "userId123";
        String userPw = "fail";
        String userName = "유저컨트롤러테스트";
        String userNickname = "userNickname";
        String address = "address";
        String addressDetail = "addressDetail";
        String pNum = "01012345678";
        String email = "dydgus625@naver.com";
        UserRequestDto userRequestDto =
                new UserRequestDto(userId, userPw, userName, userNickname, address, addressDetail, pNum, email);

        // when
        Mockito.when(userService.join(any(UserRequestDto.class)))
                .thenThrow(new CustomException(UserMessage.JOIN_FAIL));

        // then
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andExpect(jsonPath("$.message").value(UserMessage.JOIN_FAIL.message()))
                .andExpect(jsonPath("$.responseCode").value(UserMessage.JOIN_FAIL.code()));
    }

    @Test
    void loginTest_success() throws Exception{
        // given
        String userId = "ayh0123";
        String userPw = "123456h!";
        String username = "yha99";
        String nickname = "ayh0123";

        UserRequestDto requestDto =
                new UserRequestDto(userId,userPw,username,nickname,null,null,null,null);

        UserResponseDto responseDto =
                new UserResponseDto(1L, userId, username, nickname,null,null,null,null,false,null);

        // when
        Mockito.when(userService.login(any(UserRequestDto.class)))
                .thenReturn(responseDto);

        // then
        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.data.userId").value(responseDto.userId()))
                .andExpect(jsonPath("$.data.username").value(responseDto.username()))
                .andExpect(jsonPath("$.data.nickname").value(responseDto.nickname()))
                .andExpect(jsonPath("$.message").value(UserMessage.LOGGED_IN.message()))
                .andExpect(jsonPath("$.responseCode").value(HttpStatus.OK.value()));
    }

    @Test
    void loginTest_fail() throws Exception{
        // given
        String userId = "ayh0123";
        String userPw = "123456h!";
        String username = "yha99";
        String nickname = "ayh0123";

        UserRequestDto requestDto =
                new UserRequestDto(userId,userPw,username,nickname,null,null,null,null);

        // when
        Mockito.when(userService.login(any(UserRequestDto.class)))
                .thenThrow(new CustomException(UserMessage.LOGIN_FAIL));

        // then
        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andExpect(jsonPath("$.message").value(UserMessage.LOGIN_FAIL.message()))
                .andExpect(jsonPath("$.responseCode").value(UserMessage.LOGIN_FAIL.code()));
    }

}
