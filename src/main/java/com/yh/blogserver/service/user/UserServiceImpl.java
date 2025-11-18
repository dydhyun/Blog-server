package com.yh.blogserver.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yh.blogserver.dto.request.UserRequestDto;
import com.yh.blogserver.dto.response.UserResponseDto;
import com.yh.blogserver.entity.User;
import com.yh.blogserver.exception.CustomException;
import com.yh.blogserver.mapper.UserMapper;
import com.yh.blogserver.repository.user.UserRepository;
import com.yh.blogserver.util.message.UserMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
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
            throw new CustomException(UserMessage.ID_CAN_NOT_INCLUDE_SPACE);
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

        if (userPw.isEmpty() || userPw.trim().isEmpty()){
            throw new CustomException(UserMessage.PASSWORD_MUST_NOT_BE_EMPTY);
        }
        if (16 < userPw.length() || userPw.length() < 8){
            throw new CustomException(UserMessage.PASSWORD_LENGTH_MESSAGE);
        }
        if (!userPw.matches(".*[`~!@#$%^&*()_+=.,].*")) {
            throw new CustomException(UserMessage.PASSWORD_NOT_VALID_MESSAGE);
        }

        return true;
    }

    @Override
    public Map<String, String> userNicknameCheck(String userNickname) {
        HashMap<String, String> checkMsgMap = new HashMap<>();

        long countedByUserNickname = userRepository.countByNickname(userNickname);

        if (userNickname.isEmpty() || userNickname.trim().isEmpty()){
            throw new CustomException(UserMessage.NICKNAME_MUST_NOT_BE_EMPTY);
        }

        if (countedByUserNickname >= 1){
            throw new CustomException(UserMessage.INVALID_USER_NICKNAME);
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
                .orElseThrow(() -> new CustomException(UserMessage.USER_NOT_FOUND));

        if (!passwordEncoder.matches(userRequestDto.userPw(), foundUser.getUserPw())){
            throw new CustomException(UserMessage.LOGIN_FAIL);
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
            log.error(e.getMessage());
            throw new CustomException(UserMessage.AUTHENTICATED_USER_FAIL);
        }

        String userId = (String) payloadMap.get("userId");

        return userId;
    }
}
