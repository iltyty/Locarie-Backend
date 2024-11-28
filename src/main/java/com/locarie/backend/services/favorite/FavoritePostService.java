package com.locarie.backend.services.favorite;

import com.locarie.backend.domain.dto.post.PostDto;
import com.locarie.backend.domain.dto.user.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FavoritePostService {
  void favoritePost(Long userId, Long postId);

  void unfavoritePost(Long userId, Long postId);

  Page<UserDto> listFavoredBy(Long postId, Pageable pageable);

  Page<PostDto> listFavoritePosts(Long userId, Pageable pageable);

  int countFavoredBy(Long postId);

  int countFavoritePosts(Long userId);

  boolean isFavoredBy(Long userId, Long postId);
}
