package com.locarie.backend.controllers.user.images;

import com.locarie.backend.services.user.UserProfileImagesService;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
public class UserProfileImagesController {
  private final UserProfileImagesService service;

  public UserProfileImagesController(UserProfileImagesService service) {
    this.service = service;
  }

  @PostMapping("/{id}/profile-images")
  public List<String> uploadProfileImages(
      @PathVariable("id") Long id, @RequestPart("images") MultipartFile[] profileImages) {
    return service.uploadProfileImages(id, profileImages);
  }

  @GetMapping("/{id}/profile-images")
  public List<String> getProfileImages(@PathVariable("id") Long userId) {
    return service.getProfileImages(userId);
  }
}
