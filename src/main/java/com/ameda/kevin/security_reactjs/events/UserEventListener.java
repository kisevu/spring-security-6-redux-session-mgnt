package com.ameda.kevin.security_reactjs.events;/*
*
@author ameda
@project security-reactjs
*
*/

import com.ameda.kevin.security_reactjs.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventListener {
    private final EmailService emailService;

    @EventListener
    public void onUserEvent(UserEvent event){
        switch (event.getEventType()){
            case REGISTRATION -> emailService.sendNewAccountEmail(event.getUser().getFirstName(),
                    event.getUser().getEmail(),
                    (String)  event.getData().get("key"));
            case RESET_PASSWORD ->emailService.sendPasswordResetEmail(event.getUser().getFirstName(),
                    event.getUser().getEmail(),
                    (String) event.getData().get("key"));
            default ->  {}
        }
    }
}
