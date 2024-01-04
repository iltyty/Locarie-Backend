package com.locarie.backend.controllers.user;

import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.services.user.UserAvatarService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
public class UserAvatarController {
  private final UserAvatarService service;

  public UserAvatarController(@Qualifier("UserAvatarService") UserAvatarService service) {
    this.service = service;
  }

  @PostMapping("/{userId}/avatar")
  public UserDto update(
      @PathVariable("userId") Long userId, @RequestPart("avatar") MultipartFile avatar) {
    return service.updateAvatar(userId, avatar);
  }
}
