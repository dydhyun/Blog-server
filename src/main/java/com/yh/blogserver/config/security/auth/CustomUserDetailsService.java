package com.yh.blogserver.config.security.auth;

import com.yh.blogserver.entity.User;
import com.yh.blogserver.repository.user.UserRepository;
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
    public UserDetails loadUserByUsername(String userId) {

        User user = userRepository.findByUserId(userId).orElseThrow(
                ()-> new UsernameNotFoundException("USER NOT EXIST"));

        return new CustomUserDetails(user);
    }

}
