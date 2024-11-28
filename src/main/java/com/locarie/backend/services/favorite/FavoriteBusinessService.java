package com.locarie.backend.services.favorite;

import com.locarie.backend.domain.dto.post.PostDto;
import com.locarie.backend.domain.dto.user.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FavoriteBusinessService {
  void favoriteBusiness(Long userId, Long businessId);

  void unfavoriteBusiness(Long userId, Long businessId);

  Page<UserDto> listFavoredBy(Long businessId, Pageable pageable);

  Page<UserDto> listFavoriteBusinesses(Long userId, Pageable pageable);

  int countFavoredBy(Long businessId);

  int countFavoriteBusinesses(Long userId);

  boolean isFavoredBy(Long userId, Long businessId);

  List<PostDto> getLatestPostsOfFavoriteBusinesses(Long userId);
}
