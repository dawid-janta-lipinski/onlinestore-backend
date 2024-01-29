package com.example.auth.service;

import com.example.auth.model.UserDTO;
import com.example.auth.model.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserEntity createUserEntityFromUserDTO(UserDTO userDTO){
        return UserEntity.builder()
                .login(userDTO.getLogin())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .role(userDTO.getRole())
                .build();
    }
}
