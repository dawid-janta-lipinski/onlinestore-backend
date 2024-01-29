package com.example.auth.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserDTO {

    private String login;
    private String email;
    private String password;
    private Role role;
}
