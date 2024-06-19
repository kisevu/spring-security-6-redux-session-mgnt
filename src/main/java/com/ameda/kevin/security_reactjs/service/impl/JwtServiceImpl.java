package com.ameda.kevin.security_reactjs.service.impl;/*
*
@author ameda
@project security-reactjs
@
*
*/

import com.ameda.kevin.security_reactjs.domain.Token;
import com.ameda.kevin.security_reactjs.domain.TokenData;
import com.ameda.kevin.security_reactjs.domain.dto.User;
import com.ameda.kevin.security_reactjs.enumeration.TokenType;
import com.ameda.kevin.security_reactjs.function.TriConsumer;
import com.ameda.kevin.security_reactjs.security.JwtConfig;
import com.ameda.kevin.security_reactjs.service.JwtService;
import com.ameda.kevin.security_reactjs.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.ameda.kevin.security_reactjs.constant.Constants.*;
import static com.ameda.kevin.security_reactjs.enumeration.TokenType.ACCESS;
import static com.ameda.kevin.security_reactjs.enumeration.TokenType.REFRESH;
import static io.jsonwebtoken.Header.JWT_TYPE;
import static io.jsonwebtoken.Header.TYPE;
import static java.util.Arrays.stream;
import static org.springframework.boot.web.server.Cookie.SameSite.NONE;
import static org.springframework.security.core.authority.AuthorityUtils.commaSeparatedStringToAuthorityList;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl extends JwtConfig implements JwtService {
    private final UserService userService;


    private final Supplier<SecretKey> key = () ->
            Keys.hmacShaKeyFor(Decoders.BASE64.decode(getSecret()));
    private final Function<String, Claims> claimsFunction = token ->
            Jwts.parser()
                    .verifyWith(key.get())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

    private final Function<String,String> subject = token-> getClaimsValue(token,Claims::getSubject);

    /*
    *  Below if we do not have a cookie instead of throwing an exception
    *  a fake one is created on the fly to at least return one.
    *  The reason behind this we wouldn't want it to break when it encounters a null pointer exception
    * @ameda
    * */
    private final BiFunction<HttpServletRequest, String, Optional<String>> extractToken= (request,cookieName) ->
            Optional.of(stream(request.getCookies() == null ? new Cookie[]{new Cookie(EMPTY_VALUE,EMPTY_VALUE)} :request.getCookies())
                    .filter(cookie -> Objects.equals(cookieName,cookie.getName()))
                    .map(Cookie::getValue)
                    .findAny())
                    .orElse(Optional.empty());


    private final BiFunction<HttpServletRequest, String, Optional<Cookie>> extractCookie= (request,cookieName) ->
            Optional.of(stream(request.getCookies() == null ? new Cookie[]{new Cookie(EMPTY_VALUE,EMPTY_VALUE)}: request.getCookies())
                    .filter(cookie -> Objects.equals(cookieName,cookie.getName()))
                    .findAny())
                    .orElse(Optional.empty());


    /*
    *  JWTBuilder supplier
    * */

    private final Supplier<JwtBuilder>  builder = () ->
        Jwts.builder()
                .header().add(Map.of(TYPE,JWT_TYPE))
                .and()
                .audience().add(GET_ARRAYS_LLC)
                .and()
                .id(UUID.randomUUID().toString())
                .issuedAt(Date.from(Instant.now()))
                .notBefore(new Date())
                .signWith(key.get(),Jwts.SIG.HS512);

    /*
    * Token builder below
    * */

    private final BiFunction<User,TokenType, String> buildToken = (user,tokenType) ->
            Objects.equals(tokenType, ACCESS) ? builder.get()
                    .subject(user.getUserId())
                    .claim(AUTHORITIES,user.getAuthorities())
                    .claim(ROLE,user.getRole())
                    .expiration(Date.from(Instant.now().plusSeconds(getExpiration())))
                    .compact() : builder.get()
                    .subject(user.getUserId())
                    .expiration(Date.from(Instant.now().plusSeconds(getExpiration())))
                    .compact();


    /*
    * below method adds cookie to the response
    *
    * Also, some interesting point, java 1.8 adds a way for us to create a TriConsumer, but
    * we do it in a custom way , wow.
    *  and so for the below method I will be leveraging that
    * The methods the cookie and passes it as a secure cookie
    * */

    private final TriConsumer<HttpServletResponse, User,TokenType> addCookie = (response,user, tokenType) -> {
        switch (tokenType){
            case ACCESS -> {
                var accessToken = createToken(user,Token::getAccessToken);
                var cookie = new Cookie(tokenType.getValue(),accessToken);
                // inaccessible  with javascript or hacking mechanism
                cookie.setHttpOnly(true);
//                cookie.setSecure(true); //only set cookie on https
                cookie.setMaxAge(2*60);
                cookie.setPath("/"); //root path
                cookie.setAttribute("SameSite",NONE.name());
                response.addCookie(cookie);
            }
            case REFRESH -> {
                var refreshToken = createToken(user,Token::getRefreshToken);
                var cookie = new Cookie(tokenType.getValue(),refreshToken);
                // inaccessible  with javascript or hacking mechanism
                cookie.setHttpOnly(true);
//                cookie.setSecure(true); //only set cookie on https
                cookie.setMaxAge(2*60*60);
                cookie.setPath("/"); //root path
                cookie.setAttribute("SameSite",NONE.name());
                response.addCookie(cookie);
            }
        }
    };


    private final <T> T getClaimsValue(String token, Function<Claims,T> claims){
        return claimsFunction.andThen(claims).apply(token);
    }


    public Function<String, List<GrantedAuthority>> authorities = token->
            commaSeparatedStringToAuthorityList(new StringJoiner(AUTHORITY_DELIMITER)
                    .add(claimsFunction.apply(token).get(AUTHORITIES,String.class))
                    .add(ROLE_PREFIX + claimsFunction.apply(token).get(ROLE,String.class)).toString());
     /*
    @author ameda
    */
    @Override
    public String createToken(User user, Function<Token, String> tokenFunction) {
        var token = Token.builder()
                .accessToken(buildToken.apply(user, ACCESS))
                .refreshToken(buildToken.apply(user,REFRESH))
                .build();
        return tokenFunction.apply(token);
    }

    /*
    @author ameda
    */

    @Override
    public Optional<String> extractToken(HttpServletRequest request, String cookieName) {
        return extractToken.apply(request,cookieName);
    }

    /*
    @author ameda
    */
    @Override
    public void addCookie(HttpServletResponse response, User user, TokenType tokenType) {
        addCookie.accept(response,user,tokenType);
    }

    /*
    @author ameda
    */

    @Override
    public <T> T getTokenData(String token, Function<TokenData, T> tokenDataTFunction) {
        return tokenDataTFunction.apply(
                TokenData.builder()
                        .valid(Objects.equals(userService.getUserByUserId(subject.apply(token)).getUserId(),
                                claimsFunction.apply(token).getSubject()))
                        .authorities(authorities.apply(token))
                        .claims(claimsFunction.apply(token))
                        .user(userService.getUserByUserId(subject.apply(token)))
                        .build());
    }

    /*
   @author{author}
   */
    @Override
    public void removeCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        var optionalCookie = extractCookie.apply(request,cookieName);
        if(optionalCookie.isPresent()){
            var cookie = optionalCookie.get();
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }
}
