package com.locarie.backend.services.user;

import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.dto.user.UserRegistrationDto;

public interface UserRegisterService {
  UserDto register(UserRegistrationDto dto);
}
