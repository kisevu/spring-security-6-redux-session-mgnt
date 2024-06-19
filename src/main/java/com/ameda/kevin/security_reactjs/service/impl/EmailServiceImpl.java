package com.ameda.kevin.security_reactjs.service.impl;/*
*
@author ameda
@project security-reactjs
*
*/

import com.ameda.kevin.security_reactjs.exceptions.APIException;
import com.ameda.kevin.security_reactjs.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.ameda.kevin.security_reactjs.utils.EmailUtils.getEmailMessage;
import static com.ameda.kevin.security_reactjs.utils.EmailUtils.getResetPasswordMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    private static final String NEW_USER_ACCOUNT_VERIFICATION = "New user account verification";
    private static final String PASSWORD_RESET_REQUEST = "Password reset request";
    private final JavaMailSender mailSender;

    @Value("${spring.mail.verify.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    @Async
    public void sendNewAccountEmail(String name, String email, String token) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setText(getEmailMessage(name,host,token));
            mailSender.send(message);
        }catch (Exception exception){
            log.error(exception.getMessage());
            throw new APIException("Unable to send email");
        }
    }


    @Override
    @Async
    public void sendPasswordResetEmail(String name, String to, String token) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(PASSWORD_RESET_REQUEST);
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setText(getResetPasswordMessage(name,host,token));
            mailSender.send(message);
        }catch (Exception exception){
            log.error(exception.getMessage());
            throw new APIException("Unable to send email");
        }

    }
}
