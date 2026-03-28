package com.yh.blogserver.service.cleanup;

import com.yh.blogserver.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;

@Slf4j
@Service
public class UserCleanupService extends AbstractCleanupService{

    private final UserRepository userRepository;

    public UserCleanupService(UserRepository userRepository, TransactionTemplate transactionTemplate) {
        super(transactionTemplate);
        this.userRepository = userRepository;
    }

    public void deleteExpiredUsers(){
        cleanup("USER");
    }

    @Override
    protected int deleteChunk(LocalDateTime expiredTime){
        return userRepository.deleteExpiredUser(expiredTime, CHUNK_SIZE);
    }

}