package com.example.auth.service;

import com.example.auth.dao.UserDao;
import com.example.auth.model.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;

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


    public UserEntity saveUser(UserEntity userEntity){
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userDao.saveAndFlush(userEntity);
    }
    public String generateToken(String username, int exp){
        return jwtService.generateToken(username, exp);
    }
    public void validateToken(String token){
        jwtService.validateToken(token);
    }
    public void register(UserDTO userDTO) {
        //TODO validate user in exists or if not null

        saveUser(userMapper.createUserEntityFromUserDTO(userDTO));
    }

    public ResponseEntity<?> login (LoginRequest loginRequest, HttpServletResponse response) {
        UserEntity user = userDao.findUserByLogin(loginRequest.getLogin()).orElse(null);
        if (user == null) return ResponseEntity.ok(new AuthResponse(Code.A1));
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getLogin(),loginRequest.getPassword()));
        } catch (Exception e){
            return ResponseEntity.ok(new AuthResponse(Code.A2));
        }

        Cookie refresh = cookieService.generateCookie("refresh", generateToken(loginRequest.getLogin(), refreshExp), refreshExp);
        Cookie cookie = cookieService.generateCookie("Authorization", generateToken(loginRequest.getLogin(), exp), exp);
        response.addCookie(cookie);
        response.addCookie(refresh);
        return ResponseEntity.ok(LoginResponse.builder()
                .login(user.getLogin())
                .email(user.getEmail())
                .role(user.getRole())
                .build());
    }
}
