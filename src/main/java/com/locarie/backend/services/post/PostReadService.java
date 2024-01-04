package com.locarie.backend.services.post;

import com.locarie.backend.domain.dto.post.PostDto;
import java.util.List;
import java.util.Optional;

public interface PostReadService {
  Optional<PostDto> get(Long id);

  List<PostDto> list();

  List<PostDto> listNearby(double latitude, double longitude, double distance);
}
