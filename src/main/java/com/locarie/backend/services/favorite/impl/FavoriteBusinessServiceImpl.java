package com.locarie.backend.services.favorite.impl;

import com.locarie.backend.domain.dto.post.PostDto;
import com.locarie.backend.domain.dto.user.UserDto;
import com.locarie.backend.domain.entities.PostEntity;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.mapper.Mapper;
import com.locarie.backend.repositories.post.PostRepository;
import com.locarie.backend.repositories.user.UserRepository;
import com.locarie.backend.services.favorite.FavoriteBusinessService;
import com.locarie.backend.services.utils.UserFindUtils;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class FavoriteBusinessServiceImpl implements FavoriteBusinessService {
  private final UserRepository userRepository;
  private final PostRepository postRepository;

  private final UserFindUtils userFindUtils;
  private final Mapper<UserEntity, UserDto> userMapper;
  private final Mapper<PostEntity, PostDto> postMapper;

  public FavoriteBusinessServiceImpl(
      UserRepository userRepository,
      PostRepository postRepository,
      UserFindUtils userFindUtils,
      Mapper<UserEntity, UserDto> userMapper,
      Mapper<PostEntity, PostDto> postMapper) {
    this.userRepository = userRepository;
    this.postRepository = postRepository;
    this.userFindUtils = userFindUtils;
    this.userMapper = userMapper;
    this.postMapper = postMapper;
  }

  @Override
  public void favoriteBusiness(Long userId, Long businessId) {
    UserEntity user = userFindUtils.findUserById(userId);
    UserEntity businessUser = userFindUtils.findUserById(businessId);
    favoriteBusinessIfNeeded(user, businessUser);
  }

  private void favoriteBusinessIfNeeded(UserEntity user, UserEntity businessUser) {
    if (isAlreadyFavored(user, businessUser)) {
      return;
    }
    doFavoriteBusiness(user, businessUser);
  }

  private boolean isAlreadyFavored(UserEntity user, UserEntity businessUser) {
    List<UserEntity> favoredBy = businessUser.getFavoredBy();
    List<UserEntity> favoriteBusinesses = user.getFavoriteBusinesses();
    return favoredBy != null
        && favoriteBusinesses != null
        && favoredBy.stream().map(UserEntity::getId).toList().contains(user.getId())
        && favoriteBusinesses.stream()
            .map(UserEntity::getId)
            .toList()
            .contains(businessUser.getId());
  }

  private void doFavoriteBusiness(UserEntity user, UserEntity businessUser) {
    addFavoredBy(user, businessUser);
    addFavoriteBusiness(user, businessUser);
    saveUpdates(user, businessUser);
  }

  private void addFavoredBy(UserEntity user, UserEntity businessUser) {
    List<UserEntity> favoredBy = businessUser.getFavoredBy();
    if (favoredBy == null) {
      favoredBy = new ArrayList<>();
    }
    favoredBy.add(user);
    businessUser.setFavoredBy(favoredBy);
  }

  private void addFavoriteBusiness(UserEntity user, UserEntity businessUser) {
    List<UserEntity> favoriteBusinesses = user.getFavoriteBusinesses();
    if (favoriteBusinesses == null) {
      favoriteBusinesses = new ArrayList<>();
    }
    favoriteBusinesses.add(businessUser);
    user.setFavoriteBusinesses(favoriteBusinesses);
  }

  private void saveUpdates(UserEntity user, UserEntity businessUser) {
    userRepository.save(user);
    userRepository.save(businessUser);
  }

  @Override
  public void unfavoriteBusiness(Long userId, Long businessId) {
    UserEntity user = userFindUtils.findUserById(userId);
    UserEntity businessUser = userFindUtils.findUserById(businessId);
    unfavoriteBusinessIfNeeded(user, businessUser);
  }

  private void unfavoriteBusinessIfNeeded(UserEntity user, UserEntity businessUser) {
    if (!isAlreadyFavored(user, businessUser)) {
      return;
    }
    doUnfavoriteBusiness(user, businessUser);
  }

  private void doUnfavoriteBusiness(UserEntity user, UserEntity businessUser) {
    removeFavoredBy(user, businessUser);
    removeFavoriteBusiness(user, businessUser);
    saveUpdates(user, businessUser);
  }

  private void removeFavoredBy(UserEntity user, UserEntity businessUser) {
    List<UserEntity> favoredBy = businessUser.getFavoredBy();
    favoredBy.removeIf(u -> u.getId().equals(user.getId()));
    businessUser.setFavoredBy(favoredBy);
  }

  private void removeFavoriteBusiness(UserEntity user, UserEntity businessUser) {
    List<UserEntity> favoriteBusinesses = user.getFavoriteBusinesses();
    favoriteBusinesses.removeIf(u -> u.getId().equals(businessUser.getId()));
    user.setFavoriteBusinesses(favoriteBusinesses);
  }

  @Override
  public Page<UserDto> listFavoredBy(Long businessId, Pageable pageable) {
    return userRepository.listFavoredBy(businessId, pageable).map(userMapper::mapTo);
  }

  @Override
  public Page<UserDto> listFavoriteBusinesses(Long userId, Pageable pageable) {
    return userRepository.listFavoriteBusinesses(userId, pageable).map(userMapper::mapTo);
  }

  @Override
  public int countFavoredBy(Long businessId) {
    return userRepository.countFavoredBy(businessId);
  }

  @Override
  public int countFavoriteBusinesses(Long userId) {
    return userRepository.countFavoriteBusinesses(userId);
  }

  @Override
  public boolean isFavoredBy(Long userId, Long businessId) {
    return userRepository.hasBeenFollowed(userId, businessId) > 0;
  }

  @Override
  public Page<PostDto> getLatestPostsOfFavoriteBusinesses(Long userId, Pageable pageable) {
    UserEntity user = userFindUtils.findUserById(userId);
    List<Long> favoriteBusinessIds =
        user.getFavoriteBusinesses().stream().map(UserEntity::getId).toList();
    return postRepository.findByUserIds(favoriteBusinessIds, pageable).map(postMapper::mapTo);
  }
}
