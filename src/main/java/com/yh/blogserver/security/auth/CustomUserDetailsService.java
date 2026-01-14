package com.yh.blogserver.security.auth;

import com.yh.blogserver.entity.User;
import com.yh.blogserver.exception.CustomException;
import com.yh.blogserver.repository.user.UserRepository;
import com.yh.blogserver.util.message.UserMessage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public CustomUserDetails loadUserByUserId(String userId) {

        User user = userRepository.findByUserId(userId).orElseThrow(
                ()-> new CustomException(UserMessage.USER_NOT_FOUND)
        );

        return new CustomUserDetails(
                user.getUserId(),
                user.getIsAdmin(),
                user.getUserDeleteFlag()
        );
    }

}
