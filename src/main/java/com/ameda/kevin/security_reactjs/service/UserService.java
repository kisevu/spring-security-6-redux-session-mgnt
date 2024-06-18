package com.ameda.kevin.security_reactjs.service;
/*
*
@author ameda
@project security-reactjs
*
*/


import com.ameda.kevin.security_reactjs.entity.RoleEntity;
import com.ameda.kevin.security_reactjs.enumeration.Authority;

public interface UserService {
    void createUser(String firstName, String lastName, String email, String password);
    RoleEntity getRoleName(String name);
}
