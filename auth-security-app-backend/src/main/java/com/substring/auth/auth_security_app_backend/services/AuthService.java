package com.substring.auth.auth_security_app_backend.services;

import com.substring.auth.auth_security_app_backend.dtos.UserDto;

public interface AuthService {
    UserDto registerUser(UserDto userDto);

    //login user
}
