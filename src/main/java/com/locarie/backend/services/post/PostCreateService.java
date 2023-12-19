package com.locarie.backend.services.post;

import com.locarie.backend.domain.dto.PostDto;
import org.springframework.web.multipart.MultipartFile;

public interface PostCreateService {
  PostDto create(PostDto dto, MultipartFile[] images);
}
