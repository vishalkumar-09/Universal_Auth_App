package com.substring.auth.auth_security_app_backend.exceptions;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String message){
        super(message);
    }
    public ResourceNotFoundException(){
        super("Resource not found !!");
    }
}
