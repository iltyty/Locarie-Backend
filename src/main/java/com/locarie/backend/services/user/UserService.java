package com.locarie.backend.services.user;

import com.locarie.backend.domain.dto.UserDto;
import com.locarie.backend.domain.dto.UserLoginRequestDto;
import com.locarie.backend.domain.dto.UserLoginResponseDto;
import com.locarie.backend.domain.dto.UserRegistrationDto;
import java.util.List;
import java.util.Optional;

public interface UserService extends UserAvatarService {
  UserDto register(UserRegistrationDto dto);

  UserLoginResponseDto login(UserLoginRequestDto dto);

  Optional<UserDto> get(Long id);

  List<UserDto> list();
}
