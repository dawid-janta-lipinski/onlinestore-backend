package com.example.auth.service;

import com.example.auth.dao.ResetPasswordOperationsDao;
import com.example.auth.model.ResetPasswordOperationEntity;
import com.example.auth.model.UserEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@EnableScheduling
@Slf4j
public class ResetPasswordService {
    private final ResetPasswordOperationsDao resetPasswordOperationsDao;

    @Transactional
    public ResetPasswordOperationEntity initResetOperation(UserEntity user){
        log.info("--START initResetOperation");
        ResetPasswordOperationEntity resetPassword = ResetPasswordOperationEntity.builder()
                .uid(UUID.randomUUID().toString())
                .createDate(new Timestamp(System.currentTimeMillis()))
                .user(user)
                .build();

        resetPasswordOperationsDao.deleteAllByUser(user);
        log.info("--STOP initResetOperation");
        return resetPasswordOperationsDao.saveAndFlush(resetPassword);
    }

    public void endOperation(String uid){
        resetPasswordOperationsDao.findByUid(uid).ifPresent(resetPasswordOperationsDao::delete);
    }

    @Scheduled(cron = "0 0/1 * * * *")
    protected void deleteExpireOperation(){
        List<ResetPasswordOperationEntity> resetOperations = resetPasswordOperationsDao.findExpiredOperations();
        log.info("Find {} expired operations to delete",resetOperations.size());
        if (resetOperations != null && !resetOperations.isEmpty()){
            resetPasswordOperationsDao.deleteAll(resetOperations);
        }
    }

}
