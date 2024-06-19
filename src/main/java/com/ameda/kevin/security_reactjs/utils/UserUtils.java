package com.ameda.kevin.security_reactjs.utils;/*
*
@author ameda
@project security-reactjs
@
*
*/

import com.ameda.kevin.security_reactjs.domain.dto.User;
import com.ameda.kevin.security_reactjs.entity.CredentialEntity;
import com.ameda.kevin.security_reactjs.entity.RoleEntity;
import com.ameda.kevin.security_reactjs.entity.UserEntity;
import org.springframework.beans.BeanUtils;


import java.util.UUID;

import static com.ameda.kevin.security_reactjs.constant.Constants.DAYS;
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
               .mfa(false)
               .enabled(false)
               .loginAttempts(0)
               .qrCodeSecret(EMPTY)
               .phone(EMPTY)
               .bio(EMPTY)
               .imageUrl("https://cdn-icons-png.flaticon.com/512/149/149071.png")
               .role(role)
               .build();
   }

    public static User fromUserEntity(UserEntity userEntity, RoleEntity role, CredentialEntity credentialEntity) {
       User user = new User();
        BeanUtils.copyProperties(userEntity,user);
        user.setLastLogin(userEntity.getLastLogin().toString());
        user.setCredentialsNonExpired(isCredentialNonExpired(credentialEntity));
        user.setCreatedAt(userEntity.getCreatedAt().toString());
        user.setUpdatedAt(userEntity.getUpdatedAt().toString());
        user.setRole(role.getName());
        user.setAuthorities(role.getAuthorities().getValue());
        return user;
    }

    public static boolean isCredentialNonExpired(CredentialEntity credentialEntity) {
       return credentialEntity.getUpdatedAt().plusDays(DAYS).isAfter(now());
    }

}
