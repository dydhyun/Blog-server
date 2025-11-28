package com.yh.blogserver.service;


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
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

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

    @Test
    void joinTest_실패(){
        // given

        // when
        // then

    }
    @Test
    void joinTest_성공(){
        // given
        // when
        // then
    }

}