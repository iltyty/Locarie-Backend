package com.locarie.backend.services;

import com.locarie.backend.domain.dto.UserDto;
import com.locarie.backend.domain.dto.UserLoginDto;
import com.locarie.backend.domain.dto.UserRegistrationDto;
import com.locarie.backend.domain.entities.UserEntity;

import java.util.Optional;

public interface UserService {
    UserEntity createUser(UserEntity user);

    UserDto register(UserRegistrationDto dto);

    String login(UserLoginDto dto);

    Optional<UserDto> getUser(Long id);
}
