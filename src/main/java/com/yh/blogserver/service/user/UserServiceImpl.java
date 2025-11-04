package com.yh.blogserver.service.user;

import com.yh.blogserver.dto.UserDto;
import com.yh.blogserver.entity.User;
import com.yh.blogserver.repository.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        checkMsgMap.put("userIdCheckMsg", "available userId");
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

        checkMsgMap.put("userPwCheckMsg", "available userPw");
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

        checkMsgMap.put("userNicknameCheckMsg", "available nickname");
        return checkMsgMap;
    }

    @Override
    public UserDto join(UserDto userDto){

        userDto.setUserPw(passwordEncoder.encode(userDto.getUserPw()));
        User user = userDto.toEntity();

        User joinedUser = userRepository.save(user);

        UserDto joinedUserDto = joinedUser.toDto();
        joinedUserDto.setUserPw("");

        return joinedUserDto;
    }

    @Override
    public UserDto login(UserDto userDto) {

        UserDto foundUserDto = userRepository.findByUserId(userDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다.")).toDto();

        if (!passwordEncoder.matches(userDto.getUserPw(), foundUserDto.getUserPw())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        foundUserDto.setUserPw("");

        return foundUserDto;
    }

    @Override
    public UserDto getUserByUserId(String userId) {

        Optional<User> foundUserDto = userRepository.findByUserId(userId);
        
        return foundUserDto.orElseThrow().toDto();
    }
}
