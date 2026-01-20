package com.substring.auth.auth_security_app_backend.services;

import com.substring.auth.auth_security_app_backend.dtos.UserDto;

public interface UserService {
    //create user
    UserDto createUser(UserDto userDto);
    //Get User by email
    UserDto getUserByEmail(String email);
    //update user
    UserDto updateUser(UserDto userDto,String userId);
    //delete user
    void deleteUser(String userId);
    //get user by id
    UserDto getuserById(String userId);
    //get all Users
    Iterable<UserDto> getAllUsers();
}
