package com.yh.blogserver.security.auth;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Getter
public class CustomUserDetails implements UserDetails {

    private final String userId;
    private final String role;
    private final boolean userDeleted;

    public CustomUserDetails(String userId, boolean isAdmin, boolean userDeleted) {
        this.userId = userId;
        this.role = isAdmin ? "ROLE_ADMIN" : "ROLE_USER";
        this.userDeleted = userDeleted;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public boolean isEnabled() {
        return !userDeleted;
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public String getPassword() {
        return null; // JWT 기반이므로 사용 안 함
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }

}
