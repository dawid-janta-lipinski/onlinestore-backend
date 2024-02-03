package com.example.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthRequestDTO {
    private String login;
    private String password;
}
