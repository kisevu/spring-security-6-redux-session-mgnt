package com.ameda.kevin.security_reactjs.utils;/*
*
@author ameda
@project security-reactjs
@
*
*/

import com.ameda.kevin.security_reactjs.entity.RoleEntity;
import com.ameda.kevin.security_reactjs.entity.UserEntity;


import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.apache.logging.log4j.util.Strings.EMPTY;

public class UserUtils {

   public static UserEntity createUserEntity(String firstName,
                                             String lastName,
                                             String email,
                                             RoleEntity role){
       return UserEntity.builder()
               .userId(UUID.randomUUID().toString())
               .firstName(firstName)
               .lastName(lastName)
               .email(email)
               .lastLogin(now())
               .accountNonExpired(true)
               .accountNonLocked(true)
               .enabled(false)
               .loginAttempts(0)
               .qrCodeSecret(EMPTY)
               .phone(EMPTY)
               .bio(EMPTY)
               .imageUrl("https://cdn-icons-png.flaticon.com/512/149/149071.png")
               .role(role)
               .build();
   }

}
