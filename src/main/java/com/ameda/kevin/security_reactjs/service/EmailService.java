package com.ameda.kevin.security_reactjs.service;/*
*
@author ameda
@project security-reactjs
*
*/


public interface EmailService {
    void sendNewAccountEmail(String name,String to, String token);

    void sendPasswordResetEmail(String name, String to, String token);
}
