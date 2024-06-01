package com.locarie.backend.services.post;

import com.locarie.backend.domain.dto.post.PostDto;
import java.util.List;
import java.util.Optional;

public interface PostReadService {
  Optional<PostDto> get(Long id);

  List<PostDto> list();

  List<PostDto> listNearby(double latitude, double longitude, double distance);

  List<PostDto> listNearbyAll(double latitude, double longitude);

  List<PostDto> listUserPosts(Long id);

  List<PostDto> listWithin(
      double minLatitude, double maxLatitude, double minLongitude, double maxLongitude);
}
