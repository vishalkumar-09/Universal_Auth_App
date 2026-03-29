package com.substring.auth.auth_security_app_backend.dtos;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public record ApiError(
        int Status,
        String error,
        String messsage,
        String path,
        OffsetDateTime timeStamp
) {
    public static ApiError of(int Status, String error, String message, String path) {
        return new ApiError(Status,error, message,path,OffsetDateTime.now(ZoneOffset.UTC));
    }
    public static ApiError of(int Status, String error, String message, String path,boolean timeStamp) {
        return new ApiError(Status,error, message,path,null);
    }
}
