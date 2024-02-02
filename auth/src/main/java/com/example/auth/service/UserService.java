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
import org.springframework.http.HttpStatus;
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

        if (cookies.isEmpty()){
            log.info("Can't login because in token is empty");
            throw new IllegalArgumentException("Token can't be null");
        }

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
            log.info("Users alredy exist with this name");
            throw new UserExistingWithNameException("User with login " + login + " already exists.");
        });

        userDao.findUserByEmail(userRegisterDTO.getEmail()).ifPresent(email -> {
            log.info("Users alredy exist with this mail");
            throw new UserExistingWithEmailException("User with email " + email + " already exists.");
        });

        saveUser(userMapper.createUserEntityFromUserDTO(userRegisterDTO));
    }

    public ResponseEntity<?> login (LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
        log.info("--START LoginService");
        UserEntity user = userDao.findUserByLogin(loginRequestDTO.getLogin()).orElse(null);
        if (user == null){
            log.info("User dont exist");
            log.info("--STOP LoginService");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(Code.A1));
        }
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getLogin(), loginRequestDTO.getPassword()));
        } catch (Exception e){
            log.info("Incorrect login data");
            log.info("--STOP LoginService");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(Code.A2));
        }

        Cookie refresh = cookieService.generateCookie("refresh", generateToken(loginRequestDTO.getLogin(), refreshExp), refreshExp);
        Cookie authorization = cookieService.generateCookie("Authorization", generateToken(loginRequestDTO.getLogin(), exp), exp);
        response.addCookie(authorization);
        response.addCookie(refresh);
        log.info("Successfully logged in");
        log.info("--STOP LoginService");
        return ResponseEntity.ok(LoginResponseDTO.builder()
                .login(user.getLogin())
                .email(user.getEmail())
                .role(user.getRole())
                .build());
    }

    public ResponseEntity<?> loginByToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            validateToken(request, response);
            String refresh = null;
            for (Cookie cookie: Arrays.stream(request.getCookies()).toList()){
                if (cookie.getName().equals("refresh")) refresh = cookie.getValue();
            }
            String userLogin = jwtService.getSubject(refresh);
            UserEntity user = userDao.findUserByLogin(userLogin).orElse(null);
            if (user == null) {
                log.info("Can't login user don't exist");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(Code.A1));
            }
            log.info("Successfully logged-in");
            return ResponseEntity.ok(
                    LoginResponseDTO.builder()
                    .login(user.getLogin())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .build());
        } catch (ExpiredJwtException | IllegalArgumentException exception){
            log.info("Can't login token is expired or null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(Code.A5));
        }
    }

    public ResponseEntity<?> loggedIn(HttpServletRequest request, HttpServletResponse response) {
        try {
            validateToken(request, response);
            return ResponseEntity.ok(new LoginResponse(true));
        } catch (ExpiredJwtException | IllegalArgumentException exception){
            return ResponseEntity.ok(new LoginResponse(false));
        }
    }
}
