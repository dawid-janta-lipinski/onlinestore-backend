package com.example.auth.service;

import com.example.auth.dao.UserDao;
import com.example.auth.model.UserDTO;
import com.example.auth.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDao userDao;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserEntity saveUser(UserEntity userEntity){
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userDao.saveAndFlush(userEntity);
    }
    public String generateToken(String username){
        return jwtService.generateToken(username);
    }
    public void validateToken(String token){
        jwtService.validateToken(token);
    }
    public void register(UserDTO userDTO) {
        userDao.saveAndFlush(userMapper.createUserEntityFromUserDTO(userDTO));
    }
}
