package com.substring.auth.auth_security_app_backend.services.impl;

import com.substring.auth.auth_security_app_backend.dtos.UserDto;
import com.substring.auth.auth_security_app_backend.services.AuthService;
import com.substring.auth.auth_security_app_backend.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    //Other Logic before registration
    //Verify email
    //Verify password
    //default roles
    @Override
    public UserDto registerUser(UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        UserDto userDto1=userService.createUser(userDto);
        return userDto1;
    }

}
