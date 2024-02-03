package com.example.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangePasswordData {
    private String uid;
    private String password;
}
