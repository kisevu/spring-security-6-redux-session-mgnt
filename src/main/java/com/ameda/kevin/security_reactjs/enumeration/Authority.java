package com.ameda.kevin.security_reactjs.enumeration;/*
*
@author ameda
@project security-reactjs
*
*/

import static com.ameda.kevin.security_reactjs.constant.Constants.*;

public enum Authority {
    USER(USER_AUTHORITIES),
    ADMIN(ADMIN_AUTHORITIES),
    SUPER_ADMIN(SUPER_ADMIN_AUTHORITIES),
    MANAGER(MANAGER_AUTHORITIES);

    private final String value;

    Authority(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
