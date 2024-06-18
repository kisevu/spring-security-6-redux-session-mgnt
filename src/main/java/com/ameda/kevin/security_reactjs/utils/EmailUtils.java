package com.ameda.kevin.security_reactjs.utils;
/*
*
@author ameda
@project security-reactjs
*
*/

public class EmailUtils {

    public static String getEmailMessage(String name, String host, String token) {
        return "Hello " + name + "\n\nYour new account has been created. Please click on the link below to verify your account.\n\n"+
                getVerificationUrl(host,token) + "\n\nThe Support team amedakevin@gmail.com";
    }

    public static String getVerificationUrl(String host, String token) {
        return host + "/verify/account?token=" + token;
    }

    public static String getResetPasswordMessage(String name, String host, String token) {
        return "Hello " + name + "\n\nYou issued a password reset request. Please click on the link below to reset your password.\n\n"+
                getResetUrl(host,token) + "\n\nThe Support team amedakevin@gmail.com";
    }

    public static String getResetUrl(String host, String token) {
        return host + "/verify/password?token=" + token;
    }
}
