package com.example.auth.controller;

import com.example.auth.exceptions.UserDoesntExistException;
import com.example.auth.exceptions.UserExistingWithEmailException;
import com.example.auth.exceptions.UserExistingWithNameException;
import com.example.auth.model.*;
import com.example.auth.model.AuthResponse;
import com.example.auth.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping (value = "api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
     private final UserService userService;

    @PostMapping("register")
    public ResponseEntity<AuthResponse> register (@Valid @RequestBody UserRegisterDTO userRegisterDTO){
        try {
            userService.register(userRegisterDTO);
        } catch (UserExistingWithNameException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(Code.A3));
        } catch (UserExistingWithEmailException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(Code.A4));
    }
        return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationMessage handleValidationExceptions(MethodArgumentNotValidException ex){
        return new ValidationMessage(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }


    @PostMapping("login")
    public ResponseEntity<?> login (@RequestBody LoginRequestDTO user, HttpServletResponse response){
        return userService.login(user, response);
    }

    @GetMapping("validate")
    public ResponseEntity<?> validateToken (HttpServletRequest request, HttpServletResponse response){
        try {
            userService.validateToken(request,response);
            return ResponseEntity.ok(new AuthResponse(Code.PERMIT));
        } catch (ExpiredJwtException | IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(Code.A5));
        }
    }
    @GetMapping("auto-login")
    public ResponseEntity<?> autoLogin(HttpServletRequest request, HttpServletResponse response){
        return userService.loginByToken(request, response);
    }
    @GetMapping("logged-in")
    public ResponseEntity<?> loggedIn(HttpServletRequest request, HttpServletResponse response){
        return userService.loggedIn(request, response);
    }
    @GetMapping("activate")
    public ResponseEntity<AuthResponse> activateUser(@RequestParam String uid){
        try {
            userService.activateUser(uid);
            return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
        } catch (UserDoesntExistException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(Code.A1));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AuthResponse(Code.A6));
        }
    }
}
