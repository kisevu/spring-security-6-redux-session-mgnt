package com.ameda.kevin.security_reactjs.events;
/*
*
@author ameda
@project security-reactjs
*
*/


import com.ameda.kevin.security_reactjs.entity.UserEntity;
import com.ameda.kevin.security_reactjs.enumeration.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class UserEvent {
    private UserEntity user;
    private EventType eventType;
    private Map<?,?> data;
}
