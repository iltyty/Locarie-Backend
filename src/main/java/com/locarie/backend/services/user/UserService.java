package com.locarie.backend.services.user;

import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.dto.user.UserLoginRequestDto;
import com.locarie.backend.domain.dto.user.UserLoginResponseDto;
import java.util.List;
import java.util.Optional;

public interface UserService extends UserAvatarService {

  UserLoginResponseDto login(UserLoginRequestDto dto);

  Optional<UserDto> get(Long id);

  List<UserDto> list();
}
