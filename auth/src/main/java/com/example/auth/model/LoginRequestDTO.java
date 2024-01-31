package com.example.auth.model;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String login;
    private String password;
}
