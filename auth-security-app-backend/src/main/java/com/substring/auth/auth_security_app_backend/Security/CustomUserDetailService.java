package com.substring.auth.auth_security_app_backend.Security;

import com.substring.auth.auth_security_app_backend.exceptions.ResourceNotFoundException;
import com.substring.auth.auth_security_app_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
        return userRepository.findByEmail(emailId).orElseThrow(()->new ResourceNotFoundException("Invalid Email or Password!!"));
    }
}
