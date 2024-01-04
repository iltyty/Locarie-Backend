package com.locarie.backend.services.user;

import com.locarie.backend.domain.dto.user.UserDto;
import java.util.List;
import java.util.Optional;

public interface UserService extends UserAvatarService {

  Optional<UserDto> get(Long id);

  List<UserDto> list();
}
