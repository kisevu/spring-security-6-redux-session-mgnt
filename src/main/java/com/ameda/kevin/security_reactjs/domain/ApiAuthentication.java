package com.ameda.kevin.security_reactjs.domain;/*
*
@author ameda
@project security-reactjs
@
*
*/

import com.ameda.kevin.security_reactjs.domain.dto.User;
import com.ameda.kevin.security_reactjs.exceptions.APIException;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;

public class ApiAuthentication extends AbstractAuthenticationToken {
    private static final String PASSWORD_PROTECTED = "[PASSWORD_PROTECTED]";
    private static final String EMAIL_PROTECTED = "[EMAIL_PROTECTED]";
    private User user;
    private String email;
    private String password;
    private boolean authenticated;

    /*
    * constructor called for unauthenticated user
    * */
    private ApiAuthentication(String email, String password) {
        super(AuthorityUtils.NO_AUTHORITIES);
        this.email = email;
        this.password = password;
        this.authenticated = false;
    }
    /*
    * constructor called for authenticated user
    * constructors are private for users to use the helper methods
    * */
    private ApiAuthentication(User user,  Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.user = user;
        this.password = PASSWORD_PROTECTED;
        this.email = EMAIL_PROTECTED;
        this.authenticated = true;
    }

    public static ApiAuthentication unauthenticated (String email,String password){
        return new ApiAuthentication(email,password);
    }

    public static ApiAuthentication authenticated(User user,  Collection<? extends GrantedAuthority> authorities){
        return new ApiAuthentication(user,authorities);
    }

    @Override
    public Object getCredentials() {
        return PASSWORD_PROTECTED;
    }

    /*
   *
   @author ameda
   @project security-reactjs
   *
   */
    @Override
    public void setAuthenticated(boolean authenticated) {
        throw new APIException("Oops!!!, one cannot set authentication.");
    }

    /*
   *
   @author ameda
   @project security-reactjs
   *
   */
    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public Object getPrincipal() {
        return this.user;
    }
    public String getPassword(){
        return this.password;
    }
    public String getEmail(){
        return this.email;
    }

}
