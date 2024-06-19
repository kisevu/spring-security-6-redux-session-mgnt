package com.ameda.kevin.security_reactjs.service;/*
*
@author ameda
@project security-reactjs
*
*/

import com.ameda.kevin.security_reactjs.domain.Token;
import com.ameda.kevin.security_reactjs.domain.TokenData;
import com.ameda.kevin.security_reactjs.domain.dto.User;
import com.ameda.kevin.security_reactjs.enumeration.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;
import java.util.function.Function;

public interface JwtService {

    String createToken(User user, Function<Token,String> tokenFunction);
    Optional<String> extractToken(HttpServletRequest request, String cookieName);
    void addCookie(HttpServletResponse response, User user, TokenType tokenType);
    <T> T getTokenData(String token, Function<TokenData, T> tokenDataTFunction);
    void removeCookie(HttpServletRequest request, HttpServletResponse response,String cookieName);
}
