package com.ameda.kevin.security_reactjs.enumeration;/*
*
@author ameda
@project security-reactjs
*
*/

public enum TokenType {
    ACCESS("access-token"), REFRESH("refresh-token");
    private final String value;
    TokenType(String value) {
        this.value = value;
    }
    public String getValue(){
        return value;
    }
}
