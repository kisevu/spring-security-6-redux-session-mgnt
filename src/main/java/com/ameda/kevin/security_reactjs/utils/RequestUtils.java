package com.ameda.kevin.security_reactjs.utils;/*
*
@author ameda
@project security-reactjs
@
*
*/

import com.ameda.kevin.security_reactjs.domain.Response;
import com.ameda.kevin.security_reactjs.exceptions.APIException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.util.BiConsumer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.BiFunction;

import static java.time.LocalDateTime.now;
import static java.util.Collections.emptyMap;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCauseMessage;
import static org.apache.logging.log4j.util.Strings.EMPTY;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class RequestUtils {
    private static final BiConsumer<HttpServletResponse,Response> writeResponse = (((httpServletResponse, response) -> {
        try{
            var outputStream = httpServletResponse.getOutputStream();
            new ObjectMapper().writeValue(outputStream,response);
            outputStream.flush();
        }catch (Exception exception){
            throw new APIException(exception.getMessage());
        }
    }));

    private static  final BiFunction<Exception,HttpStatus,String> errorReason = (ex,status)->{
        if(status.isSameCodeAs(FORBIDDEN)){return "Not enough permission buddy!!!";}
        if(status.isSameCodeAs(UNAUTHORIZED)){return "Go head and login buddy!!!";}
        if(ex instanceof DisabledException || ex instanceof LockedException ||
        ex instanceof BadCredentialsException || ex instanceof CredentialsExpiredException
        || ex instanceof APIException) {return ex.getMessage();}
        if(status.is5xxServerError()){
            return "An internal server error has occurred, sorry!!!";
        }else{
            return "An error occurred please try again";
        }
    }
    ;
    public static Response getResponse(HttpServletRequest request,
                                       Map<?,?> data,
                                       String message,
                                       HttpStatus status){
        return new Response(now().toString(),
                status.value(),
                request.getRequestURI(),
                HttpStatus.valueOf(status.value()),
                message,
                EMPTY,
                data
                );
    }

    public static void handleErrorResponse(HttpServletRequest request,
                                      HttpServletResponse response,
                                      Exception exception){
        if(exception instanceof AccessDeniedException){
            Response apiResponse = getErrorResponse(request,response,exception,FORBIDDEN);
            writeResponse.accept(response,apiResponse);
        }
    }

    private static Response getErrorResponse(HttpServletRequest request,
                                             HttpServletResponse response,
                                             Exception exception,
                                             HttpStatus status) {
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(status.value());
        return new Response(now().toString(),
                status.value(),
                request.getRequestURI(),HttpStatus.valueOf(status.value()),
                errorReason.apply(exception,status),
                getRootCauseMessage(exception),
                emptyMap());
    }

}
