package com.locarie.backend.services.post;

import com.locarie.backend.domain.dto.post.PostDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostReadService {
  Optional<PostDto> get(Long id);

  List<PostDto> list();

  List<PostDto> listNearby(double latitude, double longitude, double distance);

  Page<PostDto> listNearbyAll(double latitude, double longitude, Pageable pageable);

  Page<PostDto> listUserPosts(Long id, Pageable pageable);

  List<PostDto> listWithin(
      double minLatitude, double maxLatitude, double minLongitude, double maxLongitude);
}
