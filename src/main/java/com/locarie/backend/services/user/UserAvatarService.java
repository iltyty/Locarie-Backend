package com.locarie.backend.services.user;

import org.springframework.web.multipart.MultipartFile;

public interface UserAvatarService {
  String updateAvatar(Long userId, MultipartFile avatar);
}
