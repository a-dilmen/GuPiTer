package com.dilmen.gupiter.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum EErrorType {
    /**
     *
     */
    BAD_REQUEST_ERROR(1201,"Invalid Parameter",BAD_REQUEST),
    AUTH_REPASSWORD_ERROR(1301,"Passwords Do Not Match",BAD_REQUEST),
    AUTH_REGISTRATION_EMAIL_ERROR(1302,"Email Already Registered Forgot Password?",BAD_REQUEST),
    AUTH_LOGIN_ERROR(1303,"Email or Password is Incorrect",BAD_REQUEST),
    INTERNAL_ERROR(3000,"Unexpected Error",INTERNAL_SERVER_ERROR),
    TOKEN_ERROR(3001,"Token Generation Error Please Try Again Later",INTERNAL_SERVER_ERROR),
    USER_NOT_FOUND(2001,"User Could Not Be Found Please Try Again Later" ,INTERNAL_SERVER_ERROR);
    private int code;
    private String message;
    private HttpStatus httpStatus;
}
