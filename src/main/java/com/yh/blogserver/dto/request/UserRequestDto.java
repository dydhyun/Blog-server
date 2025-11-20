package com.yh.blogserver.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "회원가입 등 사용자가 프론트에서 요청하는 DTO")
@Builder
public record UserRequestDto(
//        Long userIndex,
        @Schema(description = "로그인용 사용자 ID. 8글자 이상 공백 불가", example = "test1234")
        String userId,
        @Schema(description = "로그인용 사용자 비밀번호. 8글자 이상 특수문자 포함", example = "test1234!")
        String userPw,
        @Schema(description = "사용자 이름.", example = "안용현")
        String username,
        @Schema(description = "사용자 닉네임. 공백불가", example = "myNickname")
        String nickname,
        @Schema(description = "사용자 주소.", example = "대구광역시 북구")
        String address,
        String addressDetail,
        @Schema(description = "사용자 핸드폰 번호. 숫자만 입력받아 문자열로 저장", example = "01012342365")
        String pNumber,
        @Schema(description = "사용자 이메일.", example = "dydgus625@naver.com")
        String email
) {
}
