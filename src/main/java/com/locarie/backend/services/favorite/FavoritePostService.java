package com.locarie.backend.services.favorite;

import com.locarie.backend.domain.dto.post.PostDto;
import java.util.List;

public interface FavoritePostService {
  void favoritePost(Long userId, Long postId);

  void unfavoritePost(Long userId, Long postId);

  List<PostDto> listFavoritePosts(Long userId);
}
