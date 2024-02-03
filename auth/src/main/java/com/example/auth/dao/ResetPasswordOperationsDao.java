package com.example.auth.dao;

import com.example.auth.model.ResetPasswordOperationEntity;
import com.example.auth.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResetPasswordOperationsDao extends JpaRepository<ResetPasswordOperationEntity, Long> {
    @Modifying
    void deleteAllByUser(UserEntity user);
    Optional<ResetPasswordOperationEntity> findByUid(String uid);
    @Query(nativeQuery = true, value = "SELECT * FROM reset_password_operations where create_date <= current_timestamp - INTERVAL '15 minutes'")
    List<ResetPasswordOperationEntity> findExpiredOperations();


}
