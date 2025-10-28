package com.yh.blogserver.repository.user;

import com.yh.blogserver.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    long countByUserId(String userId);

    long countByNickname(String userNickname);

}