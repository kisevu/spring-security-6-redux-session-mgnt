package com.ameda.kevin.security_reactjs.domain;/*
*
@author ameda
@project security-reactjs
*
*/

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.util.Map;
/*
* For each and every response the
* format is as stipulated below
*
* */
@JsonInclude(value = JsonInclude.Include.NON_DEFAULT)
public record Response(String time,
                       int code,
                       String path,
                       HttpStatus status,
                       String message,
                       String exception,
                       Map<?,?>data) {
}
