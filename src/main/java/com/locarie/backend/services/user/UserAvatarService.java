package com.locarie.backend.services.user;

import com.locarie.backend.domain.dto.UserDto;
import org.springframework.web.multipart.MultipartFile;

public interface UserAvatarService {
  UserDto updateAvatar(Long userId, MultipartFile avatar);
}
