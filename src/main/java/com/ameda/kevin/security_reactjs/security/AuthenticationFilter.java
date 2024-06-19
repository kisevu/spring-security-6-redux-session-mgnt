package com.ameda.kevin.security_reactjs.security;/*
*
@author ameda
@project security-reactjs
@
*
*/

import com.ameda.kevin.security_reactjs.domain.ApiAuthentication;
import com.ameda.kevin.security_reactjs.domain.Response;
import com.ameda.kevin.security_reactjs.domain.dto.User;
import com.ameda.kevin.security_reactjs.dto.LoginRequest;
import com.ameda.kevin.security_reactjs.enumeration.LoginType;
import com.ameda.kevin.security_reactjs.enumeration.TokenType;
import com.ameda.kevin.security_reactjs.service.JwtService;
import com.ameda.kevin.security_reactjs.service.UserService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.Map;

import static com.ameda.kevin.security_reactjs.utils.RequestUtils.getResponse;
import static com.ameda.kevin.security_reactjs.utils.RequestUtils.handleErrorResponse;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public static final String LOGIN_PATH = "/user/login";
    private final UserService userService;
    private final JwtService jwtService;

    public AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService,JwtService jwtService) {
        super(new AntPathRequestMatcher(LOGIN_PATH, HttpMethod.POST.name()), authenticationManager);
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /*
       @author ameda
       */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
       try{
           var user = new ObjectMapper()
                   .configure(JsonParser.Feature.AUTO_CLOSE_SOURCE,true)
                   .readValue(request.getInputStream(), LoginRequest.class);
           userService.updateLoginAttempt(user.getEmail(),LoginType.LOGIN_ATTEMPT);
           var authentication = ApiAuthentication.unauthenticated(user.getEmail(),user.getPassword());
           return getAuthenticationManager().authenticate(authentication);
       }catch (Exception exception){
           log.error(exception.getMessage());
           handleErrorResponse(request,response,exception);
           return null;
       }
    }

    /*
   *
   @author ameda
   @project security-reactjs
   *
   */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {
       var user = (User) authentication.getPrincipal();
       userService.updateLoginAttempt(user.getEmail(),LoginType.LOGIN_SUCCESS);
       var httpResponse = user.isMfa() ? sendQRCode(request,user) : sendResponse(request,response,user);
       response.setContentType(APPLICATION_JSON_VALUE);
       response.setStatus(OK.value());
       var out = response.getOutputStream();
       var mapper = new ObjectMapper();
       mapper.writeValue(out,httpResponse);
       out.flush();
    }

    private Response sendResponse(HttpServletRequest request, HttpServletResponse response, User user) {
        jwtService.addCookie(response,user, TokenType.ACCESS);
        jwtService.addCookie(response,user,TokenType.REFRESH);
        return getResponse(request, Map.of("user",user),"Login success",OK);
    }

    private Response sendQRCode(HttpServletRequest request, User user) {
        return getResponse(request,Map.of("user",user),"Enter QR code",OK);
    }
}
