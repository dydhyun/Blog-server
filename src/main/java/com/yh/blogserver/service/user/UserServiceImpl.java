package com.yh.blogserver.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yh.blogserver.dto.request.UserRequestDto;
import com.yh.blogserver.dto.response.UserResponseDto;
import com.yh.blogserver.entity.User;
import com.yh.blogserver.mapper.UserMapper;
import com.yh.blogserver.repository.user.UserRepository;
import com.yh.blogserver.util.message.UserMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
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

        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException(UserMessage.ID_MUST_NOT_BE_EMPTY.message());
        }

        long countedByUserId = userRepository.countByUserId(userId);

        if (countedByUserId >= 1) {
            throw new IllegalStateException(UserMessage.INVALID_USER_ID.message());
        }

        checkMsgMap.put("checkMessage", UserMessage.AVAILABLE_USER_ID.message());
        return checkMsgMap;
    }

    @Override
    public Map<String, String> userPwCheck(String userPw) {
        HashMap<String, String> checkMsgMap = new HashMap<>();

        if (userPw.isEmpty() || userPw.trim().isEmpty()){
            throw new IllegalArgumentException(UserMessage.PASSWORD_MUST_NOT_BE_EMPTY.message());
        }
        if (16 < userPw.length() || userPw.length() < 8){
            throw new IllegalArgumentException(UserMessage.PASSWORD_LENGTH_MESSAGE.message());
        }
        if (!userPw.matches(".*[`~!@#$%^&*()_+=.,].*")) {
            throw new IllegalArgumentException("비밀번호에는 하나 이상의 특수문자가 포함되어야 합니다.");
        }

        checkMsgMap.put("checkMessage", UserMessage.AVAILABLE_USER_PW.message());
        return checkMsgMap;
    }

    @Override
    public Map<String, String> userNicknameCheck(String userNickname) {
        HashMap<String, String> checkMsgMap = new HashMap<>();

        long countedByUserNickname = userRepository.countByNickname(userNickname);

        if (userNickname.isEmpty() || userNickname.trim().isEmpty()){
            throw new IllegalArgumentException(UserMessage.NICKNAME_MUST_NOT_BE_EMPTY.message());
        }

        if (countedByUserNickname >= 1){
            throw new IllegalArgumentException(UserMessage.INVALID_USER_NICKNAME.message());
        }

        checkMsgMap.put("checkMessage", UserMessage.AVAILABLE_USER_NICKNAME.message());
        return checkMsgMap;
    }

    @Override
    public UserResponseDto join(UserRequestDto userRequestDto){

        String encodedPw = (passwordEncoder.encode(userRequestDto.userPw()));
        User user = UserMapper.fromDto(userRequestDto);
        user.setUserPw(encodedPw);

        User joinedUser = userRepository.save(user);
//        joinedUser.setUserPw("");

        return UserMapper.toUserResponseDto(joinedUser);
    }

    @Override
    public UserResponseDto login(UserRequestDto userRequestDto) {

        User foundUser = userRepository.findByUserId(userRequestDto.userId())
                .orElseThrow(() -> new IllegalArgumentException(UserMessage.USER_NOT_FOUND.message()));

        if (!passwordEncoder.matches(userRequestDto.userPw(), foundUser.getUserPw())){
            throw new IllegalArgumentException(UserMessage.LOGIN_FAIL.message());
        }

        return UserMapper.toUserResponseDto(foundUser);
    }

    @Override
    public UserResponseDto getUserByUserId(String userId) {

        Optional<User> foundUser = userRepository.findByUserId(userId);
        
        return UserMapper.toUserResponseDto(foundUser.orElseThrow());
    }

    @Override
    public User getUserEntityByUserId(String userId) {
        return userRepository.findByUserId(userId).orElseThrow();
    }

    @Override
    public String authenticatedUser(String token) {

        Base64.Decoder decoder = Base64.getDecoder();
        String[] splitToken = token.split("\\.");
        String payloadJson = new String(decoder.decode(splitToken[1]));

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> payloadMap = null;
        try {
            payloadMap = mapper.readValue(payloadJson, Map.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }

        String userId = (String) payloadMap.get("userId");

        return userId;
    }
}
