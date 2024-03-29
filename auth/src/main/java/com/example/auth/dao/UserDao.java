package com.example.auth.dao;

import com.example.auth.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findUserByLogin(String login);

    Optional<UserEntity> findUserByEmail(String email);

    Optional<UserEntity> findUserByUuid(String uid);

    @Query(nativeQuery = true, value = "SELECT * FROM users WHERE login=?1 AND islock=false AND isenabled=true")
    Optional<UserEntity> findUserByLoginIfNotLockAndEnabled(String login);

    @Query(nativeQuery = true, value = "SELECT * FROM users where login=?1 and islock=false and isenabled=true and role='ADMIN'")
    Optional<UserEntity> findUserByLoginAndLockAndEnabledAndIsAdmin(String login);

}
