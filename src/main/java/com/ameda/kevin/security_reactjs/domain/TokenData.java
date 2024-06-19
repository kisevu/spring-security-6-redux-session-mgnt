package com.ameda.kevin.security_reactjs.domain;/*
*
@author ameda
@project security-reactjs
@
*
*/

import com.ameda.kevin.security_reactjs.domain.dto.User;
import io.jsonwebtoken.Claims;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Builder
@Getter
@Setter
public class TokenData {
    private User user;
    private Claims claims;
    private boolean valid;
    private List<GrantedAuthority> authorities;
}
