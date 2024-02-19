package com.locarie.backend.services.favorite.impl;

import com.locarie.backend.domain.dto.post.PostDto;
import com.locarie.backend.domain.entities.PostEntity;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.impl.post.PostEntityDtoMapper;
import com.locarie.backend.repositories.user.UserRepository;
import com.locarie.backend.services.favorite.FavoritePostService;
import com.locarie.backend.services.utils.PostFindUtils;
import com.locarie.backend.services.utils.UserFindUtils;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class FavoritePostServiceImpl implements FavoritePostService {
  private final UserRepository userRepository;
  private final UserFindUtils userFindUtils;
  private final PostFindUtils postFindUtils;
  private final PostEntityDtoMapper postMapper;

  public FavoritePostServiceImpl(
      UserRepository userRepository,
      UserFindUtils userFindUtils,
      PostFindUtils postFindUtils,
      PostEntityDtoMapper postMapper) {
    this.userRepository = userRepository;
    this.userFindUtils = userFindUtils;
    this.postFindUtils = postFindUtils;
    this.postMapper = postMapper;
  }

  @Override
  public void favoritePost(Long userId, Long postId) {
    UserEntity user = userFindUtils.findUserById(userId);
    PostEntity post = postFindUtils.findPostById(postId);
    favoritePostIfNeeded(user, post);
  }

  private void favoritePostIfNeeded(UserEntity user, PostEntity post) {
    if (isAlreadyFavored(user, post.getId())) {
      return;
    }
    doFavoritePost(user, post);
    saveFavoritePosts(user);
  }

  private boolean isAlreadyFavored(UserEntity user, Long postId) {
    List<PostEntity> favoritePosts = user.getFavoritePosts();
    return favoritePosts != null
        && favoritePosts.stream().map(PostEntity::getId).toList().contains(postId);
  }

  private void doFavoritePost(UserEntity user, PostEntity post) {
    List<PostEntity> favoritePosts = user.getFavoritePosts();
    if (favoritePosts == null) {
      favoritePosts = new ArrayList<>();
    }
    favoritePosts.add(post);
    user.setFavoritePosts(favoritePosts);
  }

  private void saveFavoritePosts(UserEntity user) {
    userRepository.save(user);
  }

  @Override
  public void unfavoritePost(Long userId, Long postId) {
    UserEntity user = userFindUtils.findUserById(userId);
    unfavoritePostIfNeeded(user, postId);
  }

  private void unfavoritePostIfNeeded(UserEntity user, Long postId) {
    if (!isAlreadyFavored(user, postId)) {
      return;
    }
    doUnfavoritePost(user, postId);
    saveFavoritePosts(user);
  }

  private void doUnfavoritePost(UserEntity user, Long postId) {
    List<PostEntity> favoritePosts = user.getFavoritePosts();
    favoritePosts.removeIf(post -> post.getId().equals(postId));
    user.setFavoritePosts(favoritePosts);
  }

  @Override
  public List<PostDto> listFavoritePosts(Long userId) {
    UserEntity user = userFindUtils.findUserById(userId);
    return user.getFavoritePosts().stream().map(postMapper::mapTo).toList();
  }
}
