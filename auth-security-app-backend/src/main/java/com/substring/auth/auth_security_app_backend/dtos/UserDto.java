package com.substring.auth.auth_security_app_backend.dtos;

import com.substring.auth.auth_security_app_backend.entities.Provider;
import com.substring.auth.auth_security_app_backend.entities.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private UUID id;
    private String name;
    private String email;
    private String password;
    private String image;
    private boolean enable=true;
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
    private Provider provider = Provider.LOCAL;
    private Set<RoleDto> roles= new HashSet<>();
}
