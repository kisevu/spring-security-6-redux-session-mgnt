package com.ameda.kevin.security_reactjs.utils;/*
*
@author ameda
@project security-reactjs
@
*
*/

import com.ameda.kevin.security_reactjs.domain.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

import static org.apache.logging.log4j.util.Strings.EMPTY;

public class RequestUtils {

    public static Response getResponse(HttpServletRequest request,
                                       Map<?,?> data,
                                       String message,
                                       HttpStatus status){
        return new Response(LocalDateTime.now().toString(),
                status.value(),
                request.getRequestURI(),
                HttpStatus.valueOf(status.value()),
                message,
                EMPTY,
                data
                );
    }

}
