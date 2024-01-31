package com.example.auth.controller;

import com.example.auth.model.*;
import com.example.auth.model.AuthResponse;
import com.example.auth.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping (value = "api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
     private final UserService userService;

    @PostMapping("register")
    public ResponseEntity<AuthResponse> register (@RequestBody UserDTO userDTO){
        userService.register(userDTO);
        return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
    }

    @PostMapping("login")
    public ResponseEntity<?> login (@RequestBody LoginRequest user, HttpServletResponse response){
        return userService.login(user, response);
    }
}
