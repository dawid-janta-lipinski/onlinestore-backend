package com.example.auth.dao;

import com.example.auth.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDao extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findUserByLogin(String login);

    Optional<UserEntity> findUserByEmail(String email);
}
