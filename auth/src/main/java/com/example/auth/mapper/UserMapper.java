package com.example.auth.mapper;

import com.example.auth.model.UserRegisterDTO;
import com.example.auth.model.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserEntity createUserEntityFromUserDTO(UserRegisterDTO userRegisterDTO){
        return UserEntity.builder()
                .login(userRegisterDTO.getLogin())
                .email(userRegisterDTO.getEmail())
                .password(userRegisterDTO.getPassword())
                .role(userRegisterDTO.getRole())
                .isLock(true)
                .isEnabled(false)
                .build();
    }
}
