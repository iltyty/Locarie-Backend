package com.locarie.backend.services.user;

import com.locarie.backend.domain.dto.user.UserDto;
import java.util.List;

public interface UserListService {
  List<UserDto> list();
}
