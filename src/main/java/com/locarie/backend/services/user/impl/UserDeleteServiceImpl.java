package com.locarie.backend.services.user.impl;

import com.locarie.backend.domain.entities.PostEntity;
import com.locarie.backend.domain.entities.UserEntity;
import com.locarie.backend.repositories.post.PostRepository;
import com.locarie.backend.repositories.user.UserRepository;
import com.locarie.backend.services.user.UserDeleteService;
import com.locarie.backend.services.utils.UserFindUtils;
import com.locarie.backend.storage.StorageService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserDeleteServiceImpl implements UserDeleteService {
  private final UserRepository userRepository;
  private final PostRepository postRepository;
  private final StorageService storageService;
  private final UserFindUtils userFindUtils;

  public UserDeleteServiceImpl(
      UserRepository userRepository,
      PostRepository postRepository,
      StorageService storageService,
      UserFindUtils userFindUtils) {
    this.userRepository = userRepository;
    this.postRepository = postRepository;
    this.storageService = storageService;
    this.userFindUtils = userFindUtils;
  }

  @Override
  public void delete(Long id) {
    UserEntity user = userFindUtils.findUserById(id);
    deleteAllUserPosts(user);
    updatePostsFavoredBy(user);
    updateBusinessFavoredBy(user);
    deleteUserDataDir(user);
    userRepository.delete(user);
  }

  private void deleteAllUserPosts(UserEntity user) {
    postRepository.deleteAll(postRepository.findByUserId(user.getId()));
  }

  private void updatePostsFavoredBy(UserEntity user) {
    List<PostEntity> posts = user.getFavoritePosts();
    if (posts == null) {
      return;
    }
    for (PostEntity post : posts) {
      List<UserEntity> favoredBy = post.getFavoredBy();
      favoredBy.remove(user);
      post.setFavoredBy(favoredBy);
      postRepository.save(post);
    }
  }

  private void updateBusinessFavoredBy(UserEntity user) {
    List<UserEntity> businesses = user.getFavoriteBusinesses();
    if (businesses == null) {
      return;
    }
    for (UserEntity business : businesses) {
      List<UserEntity> favoredBy = business.getFavoredBy();
      favoredBy.remove(user);
      business.setFavoredBy(favoredBy);
      userRepository.save(business);
    }
  }

  private void deleteUserDataDir(UserEntity user) {
    storageService.deleteUserDataDir(user.getId());
  }
}
