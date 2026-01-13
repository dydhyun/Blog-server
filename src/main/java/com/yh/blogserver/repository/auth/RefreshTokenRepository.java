package com.yh.blogserver.repository.auth;

import com.yh.blogserver.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    @Modifying
    @Query("delete from RefreshToken r where r.expiresAt < :now")
    int deleteExpiredTokens(@Param("now") LocalDateTime now);

}
