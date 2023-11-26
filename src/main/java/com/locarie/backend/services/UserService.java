package com.locarie.backend.services;

import com.locarie.backend.domain.dto.UserDto;
import com.locarie.backend.domain.dto.UserRegistrationDto;
import com.locarie.backend.domain.entities.UserEntity;

public interface UserService {
    UserEntity createUser(UserEntity user);

    UserDto register(UserRegistrationDto dto);
}
