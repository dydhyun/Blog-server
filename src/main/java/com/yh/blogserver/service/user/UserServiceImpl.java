package com.yh.blogserver.service.user;

import com.yh.blogserver.dto.UserDto;
import com.yh.blogserver.entitiy.User;
import com.yh.blogserver.repository.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Map<String, String> userIdCheck(String userId) {
        HashMap<String, String> checkMsgMap = new HashMap<>();

        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("userId must not be empty");
        }

        long countedByUserId = userRepository.countByUserId(userId);

        if (countedByUserId < 1){
            checkMsgMap.put("userIdCheckMsg", "available userId");
        } else {
            throw new IllegalStateException("invalid userId");
        }

        return checkMsgMap;
    }

    @Override
    public Map<String, String> userPwCheck(String userPw) {
        HashMap<String, String> checkMsgMap = new HashMap<>();
        return checkMsgMap;
    }

    @Override
    public Map<String, String> userNicknameCheck(String userNickname) {
        HashMap<String, String> checkMsgMap = new HashMap<>();
        return checkMsgMap;
    }

    @Override
    public UserDto join(UserDto userDto){

        User user = userDto.toEntity();

        User joinedUser = userRepository.save(user);

        UserDto joinedUserDto = joinedUser.toDto();

        return joinedUserDto;
    }


}
