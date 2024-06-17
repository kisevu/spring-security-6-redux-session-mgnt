package com.ameda.kevin.security_reactjs.exceptions;/*
*
@author ameda
@project security-reactjs
*
*/

public class APIException extends RuntimeException{

    public APIException(String message){
        super(message);
    }

    public APIException(){
        super("An error occurred");
    }
}
