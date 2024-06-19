package com.ameda.kevin.security_reactjs.security;/*
*
@author ameda
@project security-reactjs
@
*
*/

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.ameda.kevin.security_reactjs.constant.Constants.STRENGTH;

@Configuration
public class SecurityConfiguration {

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(STRENGTH);
    }
}
