package com.locarie.backend.services.user;

import com.locarie.backend.domain.dto.user.UserDto;
import java.util.Optional;

public interface UserGetService {
  Optional<UserDto> get(Long id);
}
