package com.locarie.backend.services.favorite;

import com.locarie.backend.domain.dto.post.PostDto;
import com.locarie.backend.domain.dto.user.UserDto;
import java.util.List;

public interface FavoriteBusinessService {
  void favoriteBusiness(Long userId, Long businessId);

  void unfavoriteBusiness(Long userId, Long businessId);

  List<UserDto> listFavoredBy(Long businessId);

  List<UserDto> listFavoriteBusinesses(Long userId);

  int countFavoredBy(Long businessId);

  int countFavoriteBusinesses(Long userId);

  boolean isFavoredBy(Long userId, Long businessId);

  List<PostDto> getLatestPostsOfFavoriteBusinesses(Long userId);
}
