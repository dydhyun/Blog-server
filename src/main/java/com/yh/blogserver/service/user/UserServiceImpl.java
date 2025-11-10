package com.yh.blogserver.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yh.blogserver.dto.request.UserRequestDto;
import com.yh.blogserver.dto.response.UserResponseDto;
import com.yh.blogserver.entity.User;
import com.yh.blogserver.mapper.UserMapper;
import com.yh.blogserver.repository.user.UserRepository;
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
            throw new IllegalArgumentException("userId must not be empty");
        }

        long countedByUserId = userRepository.countByUserId(userId);

        if (countedByUserId >= 1) {
            throw new IllegalStateException("invalid userId");
        }

        checkMsgMap.put("checkMessage", "available userId");
        return checkMsgMap;
    }

    @Override
    public Map<String, String> userPwCheck(String userPw) {
        HashMap<String, String> checkMsgMap = new HashMap<>();

        if (userPw.isEmpty() || userPw.trim().isEmpty()){
            throw new IllegalArgumentException("password must not be empty");
        }
        if (16 < userPw.length() || userPw.length() < 8){
            throw new IllegalArgumentException("비밀번호는 8 ~ 16 글자 입니다.");
        }
        if (!userPw.matches(".*[`~!@#$%^&*()_+=.,].*")) {
            throw new IllegalArgumentException("비밀번호에는 하나 이상의 특수문자가 포함되어야 합니다.");
        }

        checkMsgMap.put("checkMessage", "available userPw");
        return checkMsgMap;
    }

    @Override
    public Map<String, String> userNicknameCheck(String userNickname) {
        HashMap<String, String> checkMsgMap = new HashMap<>();

        long countedByUserNickname = userRepository.countByNickname(userNickname);

        if (userNickname.isEmpty() || userNickname.trim().isEmpty()){
            throw new IllegalArgumentException("userNickname must not be empty");
        }

        if (countedByUserNickname >= 1){
            throw new IllegalArgumentException("invalid nickname");
        }

        checkMsgMap.put("checkMessage", "available nickname");
        return checkMsgMap;
    }

    @Override
    public UserResponseDto join(UserRequestDto userRequestDto){

        String encodedPw = (passwordEncoder.encode(userRequestDto.userPw()));
        User user = UserMapper.fromDto(userRequestDto);
        user.setUserPw(encodedPw);

        User joinedUser = userRepository.save(user);
        joinedUser.setUserPw("");
        UserResponseDto joinedUserDto = UserMapper.toUserResponseDto(joinedUser);

        return joinedUserDto;
    }

    @Override
    public UserResponseDto login(UserRequestDto userRequestDto) {

        User foundUser = userRepository.findByUserId(userRequestDto.userId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));

        if (!passwordEncoder.matches(userRequestDto.userPw(), foundUser.getUserPw())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
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
