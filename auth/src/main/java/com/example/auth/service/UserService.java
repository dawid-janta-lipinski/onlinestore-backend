package com.example.auth.service;

import com.example.auth.dao.UserDao;
import com.example.auth.exceptions.UserExistingWithEmailException;
import com.example.auth.exceptions.UserExistingWithNameException;
import com.example.auth.model.*;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
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
    public void validateToken(HttpServletRequest request, HttpServletResponse response) throws ExpiredJwtException, IllegalArgumentException{
        List<Cookie> cookies = Arrays.stream(request.getCookies()).toList();

        if (cookies.isEmpty()) throw new IllegalArgumentException("Token can't be null");

        String token = null;
        String refresh = null;

        for(Cookie cookie:cookies){
            if (cookie.getName().equals("Authorization")) token = cookie.getValue();
            if (cookie.getName().equals("refresh")) refresh = cookie.getValue();
        }

        try {
            jwtService.validateToken(token);
        } catch (ExpiredJwtException | IllegalArgumentException exception){
            jwtService.validateToken(refresh);
            Cookie authorization = cookieService.generateCookie("Authorization", jwtService.refreshToken(refresh,exp), exp);
            Cookie refreshCookie = cookieService.generateCookie("refresh", jwtService.refreshToken(refresh,refreshExp), refreshExp);
            response.addCookie(authorization);
            response.addCookie(refreshCookie);

        }
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
        Cookie authorization = cookieService.generateCookie("Authorization", generateToken(loginRequestDTO.getLogin(), exp), exp);
        response.addCookie(authorization);
        response.addCookie(refresh);
        return ResponseEntity.ok(LoginResponse.builder()
                .login(user.getLogin())
                .email(user.getEmail())
                .role(user.getRole())
                .build());
    }
}
