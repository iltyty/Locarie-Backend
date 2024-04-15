package com.locarie.backend.services.user;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface UserProfileImagesService {
  List<String> uploadProfileImages(Long id, MultipartFile[] images);

  List<String> getProfileImages(Long userId);
}
