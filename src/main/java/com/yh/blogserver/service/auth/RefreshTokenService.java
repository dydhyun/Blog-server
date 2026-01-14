package com.yh.blogserver.service.auth;

public interface RefreshTokenService {

    void save(String s, String refreshToken);

    void validate(String userId, String refreshToken);

    void delete(String userId);

}
