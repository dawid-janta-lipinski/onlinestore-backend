package com.example.auth.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;



@Builder
@Getter
@Setter
public class UserRegisterDTO {

    @Length(min = 5, max = 50, message = "Login should have between 5 and 50 characters")
    private String login;
    @Email
    private String email;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Length(min = 5, max = 50, message = "Password should have between 8 and 75 characters")
    private String password;
    private Role role;
}
