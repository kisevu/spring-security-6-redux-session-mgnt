package com.ameda.kevin.security_reactjs.resource;/*
*
@author ameda
@project security-reactjs
@
*
*/

import com.ameda.kevin.security_reactjs.domain.Response;
import com.ameda.kevin.security_reactjs.dto.UserRequest;
import com.ameda.kevin.security_reactjs.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;


import static com.ameda.kevin.security_reactjs.utils.RequestUtils.getResponse;
import static java.util.Collections.emptyMap;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserResource {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<Response> saveUser(@RequestBody @Valid UserRequest userRequest,
                                             HttpServletRequest servletRequest){
        userService.createUser(userRequest.getFirstName(),
                userRequest.getLastName(),
                userRequest.getEmail(),
                userRequest.getPassword());
        return ResponseEntity.created(getUri())
                .body(getResponse(servletRequest,emptyMap(),
                        "Account created. Check your email to enable account",
                        CREATED));
    }

    @GetMapping("/verify/account")
    public ResponseEntity<Response> verifyUser(@RequestParam String token,
                                             HttpServletRequest servletRequest){
        userService.verifyAccountToken(token);
        return ResponseEntity.ok()
                .body(getResponse(servletRequest,emptyMap(),
                        "Account verified successfully",
                        OK));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequest userRequest){
        UsernamePasswordAuthenticationToken unauthenticated = UsernamePasswordAuthenticationToken
                .unauthenticated(userRequest.getEmail(), userRequest.getPassword());
        // unauthenticated returns authenticated as false and has no authorities
        //authenticated has authorities and authenticated is true
        //those are plain differences between them
        Authentication authenticated = authenticationManager.authenticate(unauthenticated);
        return ResponseEntity.ok()
                .body(Map.of("user",authenticated));
    }

    private URI getUri() {
        return URI.create("");
    }
}
