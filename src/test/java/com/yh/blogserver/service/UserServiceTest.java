package com.yh.blogserver.service;


import com.yh.blogserver.dto.request.UserRequestDto;
import com.yh.blogserver.dto.response.UserResponseDto;
import com.yh.blogserver.entity.User;
import com.yh.blogserver.exception.CustomException;
import com.yh.blogserver.repository.user.UserRepository;
import com.yh.blogserver.service.user.UserServiceImpl;
import com.yh.blogserver.util.message.UserMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Base64;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl userService;


//********************************* 아이디 테스트 *********************************

    @ParameterizedTest
    @ValueSource(strings = {"유저아이디 공백 포함", " "})
    void userIdCheck_공백포함_예외(String userId) {
        System.out.println("===== 테스트 시작 =====");
        System.out.println("입력값: " + userId);
        String expected = UserMessage.CAN_NOT_INCLUDE_SPACE.message();
        System.out.println("기대 메시지: " + expected);

        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.userIdCheck(userId);
        });

        System.out.println("실제 메시지: " + exception.getMessage());
        System.out.println("======================");

        assertEquals(expected, exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {""})
    void userIdCheck_빈문자열_예외(String userId) {
        System.out.println("===== 테스트 시작 =====");
        System.out.println("입력값: " + userId);
        String expected = UserMessage.ID_MUST_NOT_BE_EMPTY.message();
        System.out.println("기대 메시지: " + expected);

        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.userIdCheck(userId);
        });

        System.out.println("실제 메시지: " + exception.getMessage());
        System.out.println("======================");

        assertEquals(expected, exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "1234567"})
    void userIdCheck_길이부족_예외(String userId) {
        System.out.println("===== 테스트 시작 =====");
        System.out.println("입력값: " + userId);
        String expected = UserMessage.ID_CAN_NOT_UNDER_8.message();
        System.out.println("기대 메시지: " + expected);

        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.userIdCheck(userId);
        });

        System.out.println("실제 메시지: " + exception.getMessage());
        System.out.println("======================");

        assertEquals(expected, exception.getMessage());
    }

    @Test
    void userIdCheck_중복아이디_예외() {
        // given
        System.out.println("===== 테스트 시작 =====");
        String userId = "duplicateId";
        System.out.println("입력값: " + userId);
        String expected = UserMessage.INVALID_USER_ID.message();
        System.out.println("기대 메시지: " + expected);

        BDDMockito.given(userRepository.countByUserId(userId)).willReturn(1L);

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.userIdCheck(userId);
        });
        System.out.println("실제 메시지: " + exception.getMessage());
        System.out.println("======================");

        // then
        assertEquals(expected, exception.getMessage());
    }

    @Test
    void userIdCheck_성공케이스() {
        // given
        System.out.println("===== 테스트 시작 =====");
        String userId = "validUserId";
        System.out.println("입력값: " + userId);
        String expected = UserMessage.AVAILABLE_USER_ID.message();
        System.out.println("기대 메시지: " + expected);

        BDDMockito.given(userRepository.countByUserId(userId)).willReturn(0L);

        // when
        Map<String, String> result = userService.userIdCheck(userId);
        System.out.println("실제 메시지: " + result.get("checkMessage"));
        System.out.println("======================");

        // then
        assertEquals(expected, result.get("checkMessage"));
    }


//********************************* 닉네임 테스트 *********************************

    @ParameterizedTest(name = "[{index}] 입력=\"{0}\" → 기대 메시지=\"{1}\"")
    @MethodSource("nicknameFailCases")
    void userNickname_실패케이스(String input, String expectedMessage) {
        // given: DB 호출이 필요한 경우만 stub
        boolean isDuplicateCase = input.equals("duplicatedNickname");

        if (isDuplicateCase) {
            BDDMockito.given(userRepository.countByNickname(input)).willReturn(1L);
        }

        // when
        CustomException exception = assertThrows(CustomException.class, () ->
                userService.userNicknameCheck(input)
        );

        // then
        assertEquals(expectedMessage, exception.getMessage());

        if (isDuplicateCase) {
            BDDMockito.verify(userRepository).countByNickname(input);
        } else {
            BDDMockito.verify(userRepository, BDDMockito.never()).countByNickname(anyString());
        }
        // userRepository mock 객체에서 메서드 호출여부 검증하겠다 선언,
        // 실제로 DB에 접근하지 않고, Mockito가 가짜 객체로 트래킹한 호출 기록을 확인
        // 중복 케이스의 경우 userRepository의 countByNickname() 이 호출됬는지 검증하고,
        // 중복 케이스가 아닌경우 BDDMockito.never() 메서드를 통해
        // userRepository의 countByNickname() 이 호출되지 않았는지 검증.
    }

    private static Stream<Arguments> nicknameFailCases() {
        return Stream.of(
                Arguments.of(" ", UserMessage.CAN_NOT_INCLUDE_SPACE.message()),
                Arguments.of("유저닉네임 공백 포함", UserMessage.CAN_NOT_INCLUDE_SPACE.message()),
                Arguments.of("", UserMessage.NICKNAME_MUST_NOT_BE_EMPTY.message()),
                Arguments.of("duplicatedNickname", UserMessage.INVALID_USER_NICKNAME.message())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"validNickname", "사용가능닉네임"})
    void userNickname_성공케이스(String nickname){
        // given
        System.out.println("===== 테스트 시작 =====");
        System.out.println("입력값: " + nickname);
        String expected = UserMessage.AVAILABLE_USER_NICKNAME.message();
        System.out.println("기대 메시지: " + expected);

        BDDMockito.given(userRepository.countByNickname(nickname)).willReturn(0L);

        // when
        Map<String, String> result = userService.userNicknameCheck(nickname);
        System.out.println("실제 메시지: " + result.get("checkMessage"));
        System.out.println("======================");

        // then
        assertEquals(expected, result.get("checkMessage"));
    }


//********************************* 패스워드 테스트 *********************************

    @ParameterizedTest(name = "[{index}] 입력=\"{0}\" → 기대 메시지=\"{1}\"")
    @MethodSource("passwordFailCases")
    void userPassword_실패케이스(String input, String expectedMessage){
        // given
        // when
        CustomException exception = assertThrows(CustomException.class, () ->
                userService.userPwCheck(input)
        );

        // then
        assertEquals(expectedMessage, exception.getMessage());
    }
    private static Stream<Arguments> passwordFailCases() {
        return Stream.of(
                Arguments.of(" ", UserMessage.CAN_NOT_INCLUDE_SPACE.message()),
                Arguments.of("비밀번호 공백 포함", UserMessage.CAN_NOT_INCLUDE_SPACE.message()),
                Arguments.of("", UserMessage.PASSWORD_MUST_NOT_BE_EMPTY.message()),
                Arguments.of("under8!", UserMessage.PASSWORD_LENGTH_MESSAGE.message()),
                Arguments.of("passwordOver16words!1", UserMessage.PASSWORD_LENGTH_MESSAGE.message()),
                Arguments.of("noContainKey", UserMessage.PASSWORD_NOT_VALID_MESSAGE.message())
        );
    }
    @TestFactory
    @DisplayName("패스워드 검증 실패 케이스 Dynamic 테스트")
    Collection<DynamicTest> dynamicPasswordFailTests() {
        return passwordFailCases()
                .map(args -> {
                    String input = (String) args.get()[0];
                    String expectedMessage = (String) args.get()[1];

                    return DynamicTest.dynamicTest(
                            "입력: \"" + input + "\" → 기대 메시지: \"" + expectedMessage + "\"",
                            () -> {
                                CustomException exception = assertThrows(CustomException.class,
                                        () -> userService.userPwCheck(input));
                                assertEquals(expectedMessage, exception.getMessage());
                            }
                    );
                })
                .toList();
    }

    @ParameterizedTest
    @ValueSource(strings = {"validPw!","availablePw!2@"})
    void userPassword_성공케이스(String password){
        // given
        System.out.println("===== 테스트 시작 =====");
        System.out.println("입력값: " + password);
        System.out.println("기대 값: true");

        // when
        Boolean result = userService.userPwCheck(password);

        // then
        System.out.println("실제 결과: " + result);
        System.out.println("======================");
        assertTrue(result);
    }


//********************************* join,login,jwt *********************************

    private final UserRequestDto failUserRequestDto
            = UserRequestDto.builder()
            .userId("invalidUserID")
            .userPw("invalidPassword")
            .username("용현")
            .nickname("실패케이스")
            .address("address")
            .addressDetail("detail")
            .pNumber("01012345678")
            .email("dydgus625@naver.com")
            .build();

    private final UserRequestDto successUserRequestDto
            = UserRequestDto.builder()
            .userId("validUserID")
            .userPw("validPw!1")
            .username("용현")
            .nickname("성공케이스")
            .address("address")
            .addressDetail("detail")
            .pNumber("01012345678")
            .email("dydgus625@naver.com")
            .build();

    // 1. 패스워드 인코딩되어 세팅되는지
    // 2. db 저장 동작하는지
    // 3. 반환값 매핑 정상동작 하는지
    @Test
    void joinTest_성공(){
        // given
        UserRequestDto requestDto = successUserRequestDto;
        System.out.println("===== 테스트 시작 =====");
        System.out.println("입력값: " + requestDto);

        // 패스워드 인코딩 mocking
        BDDMockito.given(passwordEncoder.encode(requestDto.userPw()))
                .willReturn("encodedPassword");

        // save() 호출 시 User 객체를 캡쳐
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        BDDMockito.given(userRepository.save(captor.capture()))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when
        UserResponseDto responseDto = userService.join(requestDto);

        // then
        assertNotNull(responseDto);
        assertEquals(requestDto.nickname(), responseDto.nickname());
        assertEquals(requestDto.userId(), responseDto.userId());

        // 패스워드 인코딩여부 확인을 위해 save()에 전달된 User 객체 검증
        User savedUser = captor.getValue();
        assertEquals("encodedPassword", savedUser.getUserPw());

        BDDMockito.then(userRepository).should().save(BDDMockito.any(User.class));
        System.out.println("실제 결과: " + responseDto);
        System.out.println("======================");
    }


    @Test
    void loginTest_존재하지않는유저(){
        // given
        UserRequestDto requestDto = failUserRequestDto;
        System.out.println("===== 테스트 시작 =====");
        System.out.println("입력값: " + requestDto);
        System.out.println("기대값: " + UserMessage.USER_NOT_FOUND.message());

        BDDMockito.given(userRepository.findByUserId(failUserRequestDto.userId())).willReturn(Optional.empty());

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.login(requestDto);
        });

        // then
        assertEquals(UserMessage.USER_NOT_FOUND.message(),exception.getMessage());
        BDDMockito.verify(passwordEncoder, BDDMockito.never()).matches(anyString(), anyString());
        System.out.println("실제 결과: " + exception.getMessage());
        System.out.println("======================");
    }

    @Test
    void loginTest_패스워드불일치(){
        // given
        UserRequestDto requestDto = failUserRequestDto;
        System.out.println("===== 테스트 시작 =====");
        System.out.println("입력값: " + requestDto);
        System.out.println("기대값: " + UserMessage.LOGIN_FAIL.message());

        User mockUser = User.builder()
                .userId("user1")
                .userPw("encodedPw")
                .build();

        BDDMockito.given(userRepository.findByUserId(failUserRequestDto.userId()))
                .willReturn(Optional.of(mockUser));

        BDDMockito.given(passwordEncoder.matches(requestDto.userPw(),"encodedPw"))
                .willReturn(false);

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.login(requestDto);
        });

        // then
        assertEquals(UserMessage.LOGIN_FAIL.message(), exception.getMessage());
        System.out.println("실제 결과: " + exception.getMessage());
        System.out.println("======================");
    }
    @Test
    void loginTest_로그인성공(){
        // given
        UserRequestDto requestDto = successUserRequestDto;
        System.out.println("===== 테스트 시작 =====");
        System.out.println("입력값: " + requestDto);

        User mockUser = User.builder()
                .userId(requestDto.userId())
                .userPw("encodedPw")  // 인코딩된 비밀번호라고 가정
                .nickname(requestDto.nickname())
                .email(requestDto.email())
                .build();

        BDDMockito.given(userRepository.findByUserId(requestDto.userId()))
                .willReturn(Optional.of(mockUser));

        BDDMockito.given(passwordEncoder.matches(requestDto.userPw(), "encodedPw"))
                .willReturn(true);

        // when
        UserResponseDto response = userService.login(requestDto);

        // then
        assertNotNull(response);
        assertEquals(requestDto.userId(), response.userId());
        assertEquals(requestDto.nickname(), response.nickname());
        assertEquals(requestDto.email(), response.email());

        System.out.println("실제 결과: " + response.userId());
        System.out.println("실제 결과: " + mockUser.getUserPw());
        System.out.println("======================");

        // verify: 메서드 호출되었는지 검증
        BDDMockito.verify(userRepository).findByUserId(requestDto.userId());
        BDDMockito.verify(passwordEncoder).matches(requestDto.userPw(), "encodedPw");
    }

    @Test
    void authenticatedUser_정상토큰() throws Exception {
        // given
        String payloadJson = "{\"userId\":\"testUser\"}";
        String base64Payload = Base64.getEncoder().encodeToString(payloadJson.getBytes());

        String token = "header." + base64Payload + ".signature";
        System.out.println("===== 테스트 시작 =====");
        System.out.println("입력값: " + token);

        // when
        String userId = userService.authenticatedUser(token);

        // then
        assertEquals("testUser", userId);
        System.out.println("실제 결과: " + userId);
        System.out.println("======================");
    }

    @Test
    void authenticatedUser_JSON파싱실패() {
        // given
        String brokenPayload = "not-a-json";
        String base64Payload = Base64.getEncoder().encodeToString(brokenPayload.getBytes());

        String token = "header." + base64Payload + ".signature";
        System.out.println("===== 테스트 시작 =====");
        System.out.println("입력값: " + token);

        // when & then
        CustomException exception = assertThrows(CustomException.class, () ->
                userService.authenticatedUser(token)
        );

        assertEquals(UserMessage.AUTHENTICATED_USER_FAIL.message(), exception.getMessage());
        System.out.println("실제 결과: " + exception.getMessage());
        System.out.println("======================");
    }



}