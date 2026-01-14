package com.yh.blogserver.service.auth;

import com.yh.blogserver.dto.auth.TokenPair;

public interface AuthService {

    TokenPair issue(String userId, boolean isAdmin);

    TokenPair reIssue(String refreshToken);

    void logout(String userId);
}
