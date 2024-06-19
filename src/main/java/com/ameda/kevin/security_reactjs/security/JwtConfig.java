package com.ameda.kevin.security_reactjs.security;/*
*
@author ameda
@project security-reactjs
@
*
*/

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
@Getter
@Setter
public class JwtConfig {
    @Value("${jwt.expiration}")
    private Long expiration;
    @Value("${jwt.secret}")
    private String secret;
}
