package com.yh.blogserver.repository.user;

import com.yh.blogserver.dto.response.BlogHeaderDto;
import com.yh.blogserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    long countByUserId(String userId);

    long countByNickname(String userNickname);

    Optional<User> findByUserId(String userId);

    @Query("""
        select new com.yh.blogserver.dto.response.BlogHeaderDto(
            u.userId,
            u.nickname,
            u.profileImageUrl,
            u.description,
            (
                select count(b)
                from Board b
                where b.user = u
                  and b.boardDeleteFlag = false
            )
        )
        from User u
        where u.userId in :userIds
    """)
    List<BlogHeaderDto> findBlogHeadersByUserIds(@Param("userIds") Set<String> userIds);

    @Query("""
        select new com.yh.blogserver.dto.response.BlogHeaderDto(
            u.userId,
            u.nickname,
            u.profileImageUrl,
            u.description,
            (
                select count(b)
                from Board b
                where b.user = u
                  and b.boardDeleteFlag = false
            )
        )
        from User u
        where u.userId = :userId
    """)
    Optional<BlogHeaderDto> findBlogHeaderByUserId(@Param("userId") String userId);
}