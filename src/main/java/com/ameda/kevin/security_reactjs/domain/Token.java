package com.ameda.kevin.security_reactjs.domain;
/*
*
@author ameda
@project security-reactjs
@
*
*/

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Token {
    private String accessToken;
    private String refreshToken;
}
