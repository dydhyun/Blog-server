package com.yh.blogserver.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yh.blogserver.dto.request.UserRequestDto;
import com.yh.blogserver.dto.request.UserUpdateRequestDto;
import com.yh.blogserver.dto.response.BlogHeaderDto;
import com.yh.blogserver.dto.response.PageResponse;
import com.yh.blogserver.dto.response.UserResponseDto;
import com.yh.blogserver.entity.User;
import com.yh.blogserver.exception.CustomException;
import com.yh.blogserver.mapper.UserMapper;
import com.yh.blogserver.repository.user.UserRepository;
import com.yh.blogserver.util.message.UserMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Map<String, String> userIdCheck(String userId) {
        HashMap<String, String> checkMsgMap = new HashMap<>();

        if (userId.contains(" ")){
            throw new CustomException(UserMessage.CAN_NOT_INCLUDE_SPACE);
        }
        if (userId.isEmpty()) {
            throw new CustomException(UserMessage.ID_MUST_NOT_BE_EMPTY);
        }
        if (userId.length() < 8){
            throw new CustomException(UserMessage.ID_CAN_NOT_UNDER_8);
        }

        long countedByUserId = userRepository.countByUserId(userId);

        if (countedByUserId >= 1) {
            throw new CustomException(UserMessage.INVALID_USER_ID);
        }

        checkMsgMap.put("checkMessage", UserMessage.AVAILABLE_USER_ID.message());
        return checkMsgMap;
    }

    @Override
    public Boolean userPwCheck(String userPw) {

        if (userPw.contains(" ")){
            throw new CustomException(UserMessage.CAN_NOT_INCLUDE_SPACE);
        }
        if (userPw.isEmpty()){
            throw new CustomException(UserMessage.PASSWORD_MUST_NOT_BE_EMPTY);
        }
        if (16 < userPw.length() || userPw.length() < 8){
            throw new CustomException(UserMessage.PASSWORD_LENGTH_MESSAGE);
        }
        if (!userPw.matches(".*[`~!@#$%^&*()_=.,].*")) {
            throw new CustomException(UserMessage.PASSWORD_NOT_VALID_MESSAGE);
        }

        return true;
    }

    @Override
    public Map<String, String> userNicknameCheck(String userNickname) {
        HashMap<String, String> checkMsgMap = new HashMap<>();

        if (userNickname.contains(" ")){
            throw new CustomException(UserMessage.CAN_NOT_INCLUDE_SPACE);
        }
        if (userNickname.isEmpty()){
            throw new CustomException(UserMessage.NICKNAME_MUST_NOT_BE_EMPTY);
        }

        long countedByUserNickname = userRepository.countByNickname(userNickname);

        if (countedByUserNickname >= 1){
            throw new CustomException(UserMessage.INVALID_USER_NICKNAME);
        }

        checkMsgMap.put("checkMessage", UserMessage.AVAILABLE_USER_NICKNAME.message());
        return checkMsgMap;
    }

    @Transactional
    @Override
    public UserResponseDto join(UserRequestDto userRequestDto){

        String encodedPw = (passwordEncoder.encode(userRequestDto.userPw()));
        User user = UserMapper.fromDto(userRequestDto);
        user.changePassword(encodedPw);

        User joinedUser = userRepository.save(user);
//        joinedUser.setUserPw("");

        return UserMapper.toUserResponseDto(joinedUser);
    }

//    @Override
//    public UserResponseDto login(UserRequestDto userRequestDto) {
//
//        User foundUser = userRepository.findByUserId(userRequestDto.userId())
//                .orElseThrow(() -> new CustomException(UserMessage.USER_NOT_FOUND));
//
//        if (!passwordEncoder.matches(userRequestDto.userPw(), foundUser.getUserPw())){
//            throw new CustomException(UserMessage.LOGIN_FAIL);
//        }
//
//        return UserMapper.toUserResponseDto(foundUser);
//    }

    @Override
    public UserResponseDto getUserByUserId(String userId) {

        User foundUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(UserMessage.USER_NOT_FOUND));
        
        return UserMapper.toUserResponseDto(foundUser);
    }

    @Override
    public User getUserEntityByUserId(String userId) {
        return userRepository.findByUserId(userId).orElseThrow();
    }

    @Override // 삭제예정 - 테스트 코드에서 사용중인 임시 메서드
    public String authenticatedUser(String token) {

        Base64.Decoder decoder = Base64.getDecoder();
        String[] splitToken = token.split("\\.");
        String payloadJson = new String(decoder.decode(splitToken[1]));

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> payloadMap = null;
        try {
            payloadMap = mapper.readValue(payloadJson, Map.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new CustomException(UserMessage.AUTHENTICATED_USER_FAIL);
        }

        String userId = (String) payloadMap.get("userId");

        return userId;
    }

//    @Override
//    public boolean isAdmin(String userId) {
//        return getUserByUserId(userId).isAdmin();
//    }

    @Override
    public Map<String, BlogHeaderDto> getBlogHeadersByUserIds(Set<String> userIds) {
        List<BlogHeaderDto> blogHeaderList =  userRepository.findBlogHeadersByUserIds(userIds);

        return blogHeaderList.stream().collect(Collectors.toMap(
                BlogHeaderDto::userId,
                Function.identity()
        ));
    }

    @Override
    public BlogHeaderDto getBlogHeader(String userId) {

        return userRepository.findBlogHeaderByUserId(userId)
                .orElseThrow(() -> new CustomException(UserMessage.USER_NOT_FOUND));
    }


    @Override
    @Transactional
    public void updateMyPage(String userId, UserUpdateRequestDto userUpdateRequestDto) {
        log.info("UPDATE MYPAGE 메서드 실행");

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(UserMessage.USER_NOT_FOUND));

        if (userUpdateRequestDto.userPw() != null) {
            userPwCheck(userUpdateRequestDto.userPw());
            String encoded = passwordEncoder.encode(userUpdateRequestDto.userPw());
            user.changePassword(encoded);
        }

        if (userUpdateRequestDto.nickname() != null) {
            userNicknameCheck(userUpdateRequestDto.nickname());
        }

        user.update(userUpdateRequestDto);
    }

    @Override
    @Transactional
    public void deleteMyAccount(String userId) {
        log.info("DELETE MY_ACCOUNT 메서드 실행");

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(UserMessage.USER_NOT_FOUND));

        user.markAsDeleted();
    }

    @Override
    public PageResponse<UserResponseDto> getDeletedUser(Pageable pageable) {
        log.info("GET DELETED_USER 메서드 실행");

        Page<User> foundUserList = userRepository.findByUserDeleteFlagTrue(pageable);

        Page<UserResponseDto> userResponseDtosPage =
                foundUserList.map(UserMapper::toUserResponseDto);

        return PageResponse.from(userResponseDtosPage);
    }

}
