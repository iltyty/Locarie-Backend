package com.locarie.backend.services.user;

import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.dto.user.UserUpdateDto;

public interface UserUpdateService {
  UserDto updateUser(UserUpdateDto dto);
}
