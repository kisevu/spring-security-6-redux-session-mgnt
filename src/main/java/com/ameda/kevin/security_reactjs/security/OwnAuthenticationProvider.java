package com.ameda.kevin.security_reactjs.security;/*
*
@author ameda
@project security-reactjs
@
*
*/

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OwnAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var user = (UsernamePasswordAuthenticationToken) authentication;
        var userFromDB = userDetailsService.loadUserByUsername((String) user.getPrincipal());
        var password = (String) user.getCredentials();
        if(password.equals(userFromDB.getPassword())){
            return UsernamePasswordAuthenticationToken
                    .authenticated(userFromDB,
                    "[MASKED PASSWORD]",
                    userFromDB.getAuthorities());
        }
        throw new BadCredentialsException("unable to login user");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken
                .class
                .isAssignableFrom(authentication);
    }
}
