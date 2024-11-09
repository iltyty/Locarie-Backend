package com.locarie.backend.services.user;

import com.locarie.backend.domain.dto.user.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserListService {
  Page<UserDto> list(Pageable pageable);

  Page<UserDto> listBusinesses(Pageable pageable);
}
