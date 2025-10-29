package com.yh.blogserver.repository.user;

import com.yh.blogserver.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    long countByUserId(String userId);

    long countByNickname(String userNickname);

    Optional<User> findByUserId(String userId);
}