package com.example.auth.service;

import com.example.auth.dao.UserDao;
import com.example.auth.exceptions.UserExistingWithEmailException;
import com.example.auth.exceptions.UserExistingWithNameException;
import com.example.auth.model.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDao userDao;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CookieService cookieService;
    @Value("${jwt.exp}")
    private int exp;
    @Value("${jwt.refresh.exp}")
    private int refreshExp;


    private void saveUser(UserEntity userEntity){
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userDao.saveAndFlush(userEntity);
    }
    private String generateToken(String username, int exp){
        return jwtService.generateToken(username, exp);
    }
    private void validateToken(String token){
        jwtService.validateToken(token);
    }
    public void register(UserRegisterDTO userRegisterDTO) {
        userDao.findUserByLogin(userRegisterDTO.getLogin()).ifPresent( login -> {
            throw new UserExistingWithNameException("User with login " + login + " already exists.");
        });

        userDao.findUserByEmail(userRegisterDTO.getEmail()).ifPresent(email -> {
            throw new UserExistingWithEmailException("User with email " + email + " already exists.");
        });

        saveUser(userMapper.createUserEntityFromUserDTO(userRegisterDTO));
    }

    public ResponseEntity<?> login (LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
        UserEntity user = userDao.findUserByLogin(loginRequestDTO.getLogin()).orElse(null);
        if (user == null) return ResponseEntity.ok(new AuthResponse(Code.A1));
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getLogin(), loginRequestDTO.getPassword()));
        } catch (Exception e){
            return ResponseEntity.ok(new AuthResponse(Code.A2));
        }

        Cookie refresh = cookieService.generateCookie("refresh", generateToken(loginRequestDTO.getLogin(), refreshExp), refreshExp);
        Cookie cookie = cookieService.generateCookie("Authorization", generateToken(loginRequestDTO.getLogin(), exp), exp);
        response.addCookie(cookie);
        response.addCookie(refresh);
        return ResponseEntity.ok(LoginResponse.builder()
                .login(user.getLogin())
                .email(user.getEmail())
                .role(user.getRole())
                .build());
    }
}
