package com.ameda.kevin.security_reactjs.config;/*
*
@author ameda
@project security-reactjs
*
*/

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DotENVLoader {

    @Bean
    public Dotenv dotenv(){
        return Dotenv.
                configure()
                .load();
    }

//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder(11);
//    }
}
