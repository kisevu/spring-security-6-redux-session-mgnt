package com.ameda.kevin.security_reactjs.domain;

/*
*
@author ameda
@project security-reactjs
*
*/

/*
* NB : all non-persistable classes exists in domain package
* They are what I can term as utility classes
* The classes typically fetches info from threads
* */

public class RequestContext {
    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();
    private RequestContext(){}

    public static void start(){
        USER_ID.remove();
    }
    public static void setUserId(Long userId){
        USER_ID.set(userId);
    }
    public static Long getUserId(){
        return USER_ID.get();
    }
}
