package com.locarie.backend.services.user;

import com.locarie.backend.domain.dto.user.UserLoginRequestDto;
import com.locarie.backend.domain.dto.user.UserLoginResponseDto;

public interface UserLoginService {
  UserLoginResponseDto login(UserLoginRequestDto dto);
}
