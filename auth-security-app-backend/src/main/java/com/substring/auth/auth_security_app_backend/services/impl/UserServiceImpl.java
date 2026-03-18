package com.substring.auth.auth_security_app_backend.services.impl;

import com.substring.auth.auth_security_app_backend.dtos.UserDto;
import com.substring.auth.auth_security_app_backend.entities.Provider;
import com.substring.auth.auth_security_app_backend.entities.User;
import com.substring.auth.auth_security_app_backend.exceptions.ResourceNotFoundException;
import com.substring.auth.auth_security_app_backend.helpers.UserHelper;
import com.substring.auth.auth_security_app_backend.repositories.UserRepository;
import com.substring.auth.auth_security_app_backend.services.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {

        if(userDto.getEmail()==null || userDto.getEmail().isBlank()){
            throw new IllegalArgumentException("Email is Required");
        }
        if(userRepository.existsByEmail(userDto.getEmail())){
            throw new IllegalArgumentException("User wtih given Email is already exists");
        }

        User user = modelMapper.map(userDto, User.class);
        user.setProvider(user.getProvider()!=null ? user.getProvider() : Provider.LOCAL);
        user.setPassword(userDto.getPassword());
        //Here role to be assigned to the new user for the authorization
        //TODO:
        userRepository.save(user);
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new ResourceNotFoundException("User not found with given Email Id "));

        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        UUID uid = UserHelper.parseUUID(userId);
        User existinguser=userRepository
                .findById(uid).
                orElseThrow(()-> new ResourceNotFoundException("User not found with the give id"));
        if(userDto.getImage()!=null) existinguser.setImage(userDto.getImage());
        if(userDto.getName()!=null) existinguser.setName(userDto.getName());
        //TODO: change the password updation logic...
        if(userDto.getPassword()!=null) existinguser.setPassword(userDto.getPassword());
        if(userDto.getProvider()!=null) existinguser.setProvider(userDto.getProvider());
        existinguser.setEnable(userDto.isEnable());
        existinguser.setUpdatedAt(Instant.now());
        User updatedUser = userRepository.save(existinguser);
        return modelMapper.map(updatedUser,UserDto.class);
    }

    @Override
    public void deleteUser(String userId) {
        UUID uid=UserHelper.parseUUID(userId);
        User user=userRepository
                .findById(uid)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with the give id"));
        userRepository.delete(user);
    }

    @Override
    public UserDto getuserById(String userId) {
        UUID uid=UserHelper.parseUUID(userId);
        User user = userRepository
                .findById(uid)
                .orElseThrow(()->new ResourceNotFoundException("User not found with the give id"));
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    @Transactional
    public Iterable<UserDto> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(user -> modelMapper.map(user,UserDto.class)).toList();
    }
}
