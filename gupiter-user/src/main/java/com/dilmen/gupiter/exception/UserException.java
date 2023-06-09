package com.dilmen.gupiter.exception;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException{
    private final EErrorType EErrorType;

    /**
     * @param EErrorType
     */
    public AuthException(EErrorType EErrorType){
        super(EErrorType.getMessage());
        this.EErrorType = EErrorType;
    }

    public AuthException(EErrorType EErrorType, String message){
        super(message);
        this.EErrorType = EErrorType;
    }
}
