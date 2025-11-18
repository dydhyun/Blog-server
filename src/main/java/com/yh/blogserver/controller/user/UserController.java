package com.yh.blogserver.controller.user;

import com.yh.blogserver.config.JwtTokenProvider;
import com.yh.blogserver.dto.response.ResponseDto;
import com.yh.blogserver.dto.request.UserRequestDto;
import com.yh.blogserver.dto.response.UserResponseDto;
import com.yh.blogserver.service.user.UserService;
import com.yh.blogserver.util.message.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "User API", description = "유저 회원가입, 로그인 관련 API")
@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    private static final String CHECK_MSG_KEY = "checkMessage";

    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Operation(
            summary = "아이디 중복체크",
            description = "회원가입 폼에서 유저아이디 문자열을 받아 중복 체크 하기 위한 엔드포인트"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용 가능한 아이디"),
            @ApiResponse(responseCode = "400", description = "사용할 수 없는 아이디")
    })
    @GetMapping("/exists/id")
    public ResponseEntity<ResponseDto<Map<String, String>>> userIdCheck(@RequestParam String userId){
        log.info("[userIdCheck 요청] userId={}", userId);

        Map<String, String> checkMsgMap = userService.userIdCheck(userId);

        return ResponseEntity
                .ok(ResponseDto.success(checkMsgMap, checkMsgMap.get(CHECK_MSG_KEY), HttpStatus.OK.value()));
    }

    @Operation(
            summary = "닉네임 중복체크",
            description = "회원가입 폼에서 유저닉네임 문자열을 받아 중복 체크 하기 위한 엔드포인트"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용 가능한 닉네임"),
            @ApiResponse(responseCode = "400", description = "사용할 수 없는 닉네임")
    })
    @GetMapping("/exists/nickname")
    public ResponseEntity<ResponseDto<Map<String, String>>> userNicknameCheck(@RequestParam String userNickname){
        log.info("[userNicknameCheck 요청] userNickname={}", userNickname);

        Map<String, String> checkMsgMap = userService.userNicknameCheck(userNickname);

        return ResponseEntity
                .ok(ResponseDto.success(checkMsgMap, checkMsgMap.get(CHECK_MSG_KEY), HttpStatus.OK.value()));
    }

    @Operation(
            summary = "유저 회원가입",
            description = """
                새로운 유저 계정을 저장합니다.
                
                유저가 작성한 폼을 UserRequestDto 형식으로 받으며,
                서버에서는 비밀번호 규칙 검증(8~16자, 특수문자 필수)을 포함합니다.
                
                검증이 통과하면 DB에 계정을 생성하고
                생성된 유저 정보를 UserResponseDto로 반환합니다.
                
                회원가입은 아이디, 닉네임, 비밀번호 검증이 끝난 후에 가능하며,
                이전엔 프론트에서 요청을 막아둡니다.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @ApiResponse(responseCode = "403", description = "회원가입 실패")
    })
    @PostMapping("")
    public ResponseEntity<ResponseDto<UserResponseDto>> join(@RequestBody UserRequestDto userRequestDto){
        log.info("[USER JOIN 요청] userRequestDto={}", userRequestDto);

        userService.userPwCheck(userRequestDto.userPw());

        UserResponseDto userResponseDto = userService.join(userRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseDto.success(userResponseDto, ResponseMessage.CREATED.message(), HttpStatus.CREATED.value()));
    }

    @Operation(
            summary = "로그인",
            description = """
                    아이디와 비밀번호를 입력하여 로그인합니다.
                    성공 시 Authorization 헤더에 JWT Access Token을 담아 반환합니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "로그인 실패")
    })
    @PostMapping("/login")
    public ResponseEntity<ResponseDto<UserResponseDto>> login(@RequestBody UserRequestDto loginRequest){
        log.info("[USER LOGIN 요청] userRequestDto={}", loginRequest);

        UserResponseDto loginedUserDto = userService.login(loginRequest);

        String token = jwtTokenProvider.createToken(loginedUserDto.userId(), loginedUserDto.isAdmin());
//        String refreshToken = jwtTokenProvider.createRefreshToken(loginedUserDto.getUserId());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Bearer " + token);
        // http 표준 규약 -> Authorization: <type> <credentials>

        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders)
                .body(ResponseDto.success(loginedUserDto, ResponseMessage.OK.message(), HttpStatus.OK.value()));
    }

}