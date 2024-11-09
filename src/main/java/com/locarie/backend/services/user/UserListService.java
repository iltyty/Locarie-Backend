package com.locarie.backend.services.user;

import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.dto.user.UserLocationDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserListService {
  Page<UserDto> list(Pageable pageable);

  Page<UserDto> listBusinesses(Pageable pageable);

  List<UserLocationDto> listAllBusinesses();
}
