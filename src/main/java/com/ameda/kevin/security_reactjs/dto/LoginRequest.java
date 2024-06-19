package com.ameda.kevin.security_reactjs.dto;/*
*
@author ameda
@project security-reactjs
@
*
*/

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequest {
    @NotEmpty(message = "Email cannot be empty or null")
    @Email(message = "Invalid email address ")
    private String email;
    @NotEmpty(message = "password cannot be empty or null")
    private String password;
}
