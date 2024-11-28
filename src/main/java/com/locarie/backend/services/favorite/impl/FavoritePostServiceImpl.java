package com.locarie.backend.services.favorite.impl;

import com.locarie.backend.domain.dto.post.PostDto;
import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.entities.PostEntity;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.mapper.impl.post.PostEntityDtoMapper;
import com.locarie.backend.repositories.post.PostRepository;
import com.locarie.backend.repositories.user.UserRepository;
import com.locarie.backend.services.favorite.FavoritePostService;
import com.locarie.backend.services.utils.PostFindUtils;
import com.locarie.backend.services.utils.UserFindUtils;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class FavoritePostServiceImpl implements FavoritePostService {
  private final UserRepository userRepository;
  private final PostRepository postRepository;
  private final UserFindUtils userFindUtils;
  private final PostFindUtils postFindUtils;
  private final Mapper<UserEntity, UserDto> userMapper;
  private final Mapper<PostEntity, PostDto> postMapper;

  public FavoritePostServiceImpl(
      UserRepository userRepository,
      PostRepository postRepository,
      UserFindUtils userFindUtils,
      PostFindUtils postFindUtils,
      Mapper<UserEntity, UserDto> userMapper,
      PostEntityDtoMapper postMapper) {
    this.userRepository = userRepository;
    this.postRepository = postRepository;
    this.userFindUtils = userFindUtils;
    this.postFindUtils = postFindUtils;
    this.userMapper = userMapper;
    this.postMapper = postMapper;
  }

  @Override
  public void favoritePost(Long userId, Long postId) {
    UserEntity user = userFindUtils.findUserById(userId);
    PostEntity post = postFindUtils.findPostById(postId);
    favoritePostIfNeeded(user, post);
  }

  private void favoritePostIfNeeded(UserEntity user, PostEntity post) {
    if (isAlreadyFavored(user, post)) {
      return;
    }
    doFavoritePost(user, post);
  }

  private boolean isAlreadyFavored(UserEntity user, PostEntity post) {
    List<UserEntity> favoredBy = post.getFavoredBy();
    List<PostEntity> favoritePosts = user.getFavoritePosts();
    return favoredBy != null
        && favoritePosts != null
        && favoredBy.stream().map(UserEntity::getId).toList().contains(user.getId())
        && favoritePosts.stream().map(PostEntity::getId).toList().contains(post.getId());
  }

  private void doFavoritePost(UserEntity user, PostEntity post) {
    addFavoredBy(user, post);
    addFavoritePosts(user, post);
    saveUpdates(user, post);
  }

  private void addFavoredBy(UserEntity user, PostEntity post) {
    List<UserEntity> favoredBy = post.getFavoredBy();
    if (favoredBy == null) {
      favoredBy = new ArrayList<>();
    }
    favoredBy.add(user);
    post.setFavoredBy(favoredBy);
  }

  private void addFavoritePosts(UserEntity user, PostEntity post) {
    List<PostEntity> favoritePosts = user.getFavoritePosts();
    if (favoritePosts == null) {
      favoritePosts = new ArrayList<>();
    }
    favoritePosts.add(post);
    user.setFavoritePosts(favoritePosts);
  }

  private void saveUpdates(UserEntity user, PostEntity post) {
    userRepository.save(user);
    postRepository.save(post);
  }

  @Override
  public void unfavoritePost(Long userId, Long postId) {
    UserEntity user = userFindUtils.findUserById(userId);
    PostEntity post = postFindUtils.findPostById(postId);
    unfavoritePostIfNeeded(user, post);
  }

  private void unfavoritePostIfNeeded(UserEntity user, PostEntity post) {
    if (!isAlreadyFavored(user, post)) {
      return;
    }
    doUnfavoritePost(user, post);
  }

  private void doUnfavoritePost(UserEntity user, PostEntity post) {
    removeFavoredBy(user, post);
    removeFavoritePost(user, post);
    saveUpdates(user, post);
  }

  private void removeFavoredBy(UserEntity user, PostEntity post) {
    List<UserEntity> favoredBy = post.getFavoredBy();
    favoredBy.removeIf(u -> u.getId().equals(user.getId()));
    post.setFavoredBy(favoredBy);
  }

  private void removeFavoritePost(UserEntity user, PostEntity post) {
    List<PostEntity> favoritePosts = user.getFavoritePosts();
    favoritePosts.removeIf(p -> p.getId().equals(post.getId()));
    user.setFavoritePosts(favoritePosts);
  }

  @Override
  public Page<UserDto> listFavoredBy(Long postId, Pageable pageable) {
    return postRepository.listFavoredBy(postId, pageable).map(userMapper::mapTo);
  }

  @Override
  public Page<PostDto> listFavoritePosts(Long userId, Pageable pageable) {
    return postRepository.listFavoritePosts(userId, pageable).map(postMapper::mapTo);
  }

  @Override
  public int countFavoredBy(Long postId) {
    return postRepository.countFavoredBy(postId);
  }

  @Override
  public int countFavoritePosts(Long userId) {
    return postRepository.countFavoritePosts(userId);
  }

  @Override
  public boolean isFavoredBy(Long userId, Long postId) {
    return postRepository.hasBeenSaved(userId, postId) > 0;
  }
}
