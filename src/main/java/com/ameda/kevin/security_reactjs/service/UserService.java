package com.ameda.kevin.security_reactjs.service;
/*
*
@author ameda
@project security-reactjs
*
*/


import com.ameda.kevin.security_reactjs.domain.dto.User;
import com.ameda.kevin.security_reactjs.entity.CredentialEntity;
import com.ameda.kevin.security_reactjs.entity.RoleEntity;
import com.ameda.kevin.security_reactjs.enumeration.LoginType;

public interface UserService {
    void createUser(String firstName, String lastName, String email, String password);
    RoleEntity getRoleName(String name);
    void verifyAccountToken(String token);
    void updateLoginAttempt(String email, LoginType loginType);
    User getUserByUserId(String userId);
    User getUserByEmail(String email);
    CredentialEntity getUserCredentialById(Long id);
}
