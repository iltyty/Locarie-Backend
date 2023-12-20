package com.locarie.backend.controllers.user;

import com.locarie.backend.domain.dto.UserDto;
import com.locarie.backend.services.user.UserAvatarService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
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
    return service.update(userId, avatar);
  }

  @GetMapping(value = "/{userId}/avatar", produces = MediaType.IMAGE_JPEG_VALUE)
  public Resource get(@PathVariable("userId") Long userId) {
    byte[] bytes = service.getAvatar(userId);
    return new ByteArrayResource(bytes);
  }
}
