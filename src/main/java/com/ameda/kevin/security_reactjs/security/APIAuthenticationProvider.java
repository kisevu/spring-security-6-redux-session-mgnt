package com.ameda.kevin.security_reactjs.security;/*
*
@author ameda
@project security-reactjs
@
*
*/

import com.ameda.kevin.security_reactjs.domain.ApiAuthentication;
import com.ameda.kevin.security_reactjs.domain.UserPrincipal;
import com.ameda.kevin.security_reactjs.exceptions.APIException;
import com.ameda.kevin.security_reactjs.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.ameda.kevin.security_reactjs.constant.Constants.DAYS;

@Component
@RequiredArgsConstructor
public class APIAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final BCryptPasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var apiAuthentication = authenticationFunction.apply(authentication);
        var user = userService.getUserByEmail(apiAuthentication.getEmail());
        if(user!=null) {
            var userCredential = userService.getUserCredentialById(user.getId());
            if(userCredential.getUpdatedAt().minusDays(DAYS).isAfter(LocalDateTime.now())){
                throw new APIException("credentials are expired,reset your password.");
            }
            var userPrincipal = new UserPrincipal(user, userCredential);
            validAccount.accept(userPrincipal);
            if(encoder.matches(apiAuthentication.getPassword(),userCredential.getPassword())){
                return ApiAuthentication.authenticated(user,userPrincipal.getAuthorities());
            }else{
                throw new BadCredentialsException("Email and/or password incorrect. Try again.");
            }
        }  throw new APIException("unable to login or authenticate");
    }



    @Override
    public boolean supports(Class<?> authentication) {
        return ApiAuthentication.class.isAssignableFrom(authentication);
    }

    private  final Function<Authentication, ApiAuthentication> authenticationFunction  = authentication ->
            (ApiAuthentication) authentication;

    private final Consumer<UserPrincipal> validAccount = userPrincipal -> {
        if(userPrincipal.isAccountNonLocked()){throw new LockedException("Your account is currently locked");}

        if(userPrincipal.isEnabled()){throw new DisabledException("Your account is currently disabled");}

        if(userPrincipal.isCredentialsNonExpired()){throw new CredentialsExpiredException("Your password has expired. Update your password to proceed.");}

        if(userPrincipal.isAccountNonExpired()){throw new DisabledException("Account is currently expired. Contact your administrator");}
    };

}
